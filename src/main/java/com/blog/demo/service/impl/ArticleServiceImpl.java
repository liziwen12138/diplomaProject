package com.blog.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.demo.constant.RedisPrefixConst;
import com.blog.demo.dto.*;
import com.blog.demo.entity.Article;
import com.blog.demo.entity.ArticleTag;
import com.blog.demo.entity.Category;
import com.blog.demo.entity.Tag;
import com.blog.demo.enums.ArticleStatusEnum;
import com.blog.demo.enums.StatusCodeEnum;
import com.blog.demo.exception.BizException;
import com.blog.demo.mapper.ArticleMapper;
import com.blog.demo.mapper.ArticleTagMapper;
import com.blog.demo.mapper.CategoryMapper;
import com.blog.demo.mapper.TagMapper;
import com.blog.demo.service.ArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.demo.service.ArticleTagService;
import com.blog.demo.service.TagService;
import com.blog.demo.util.BeanCopyUtils;
import com.blog.demo.util.PageUtils;
import com.blog.demo.util.RedisUtils;
import com.blog.demo.util.UserUtils;
import com.blog.demo.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

import static com.blog.demo.constant.CommonConst.ARTICLE_SET;
import static com.blog.demo.constant.CommonConst.FALSE;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liziwen
 * @since 2022-03-16
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private ArticleTagMapper articleTagMapper;
    @Autowired
    private TagService tagService;
    @Autowired
    private ArticleTagService articleTagService;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private HttpSession session;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdateArticle(ArticleVO articleVO) {
        //保存文章类型，并返回类型数据结构
        Category category = saveArticleCategory(articleVO);
        //将前端返回的展现视图转化成后端article数据结构
        Article article = BeanCopyUtils.copyObject(articleVO, Article.class);
        //将文章类型装填到文章的数据结构中,因为前端返回的是category名，在saveArticleCategory中根据category新建或返回category对象，
        // 而article中存放的是categoryId，需要额外绑定
        if(Objects.nonNull(category)){
           article.setCategoryId(category.getId());
        }
        //为文章添加作者
        article.setUserId(UserUtils.getLoginUser().getUserInfoId());
        //调用mybatis-plus提供的存储接口
        this.saveOrUpdate(article);
        //绑定标签和文章
        saveArticleTag(articleVO,article.getId());
    }

    @Override
    public String saveArticleImages(MultipartFile file) {
        return null;
    }

    @Override
    public PageResult<ArticleBackDTO> listArticleBacks(ConditionVo conditionVo) {
        //查询文章总数
        Integer articleNumber = articleMapper.countArticleBacks(PageUtils.getCurrent(), PageUtils.getSize(), conditionVo);
        //处理文章为0的情况
        if(articleNumber <= 0){
            new PageResult<List<ArticleBackDTO>>();
        }
        //查询后台的文章（根据分页格式），并以列表的形式返回
        List<ArticleBackDTO> articleBackDTOList = articleMapper.listArticleBacks(PageUtils.getLimitCurrent(), PageUtils.getSize(), conditionVo);
        //查询文章点赞量和浏览量（直接获取对应关键字的redis表结构）
        Map<Object, Double> ViewMap = redisUtils.zAllScore(RedisPrefixConst.ARTICLE_VIEWS_COUNT);
        Map likeMap = redisUtils.hGetAll(RedisPrefixConst.ARTICLE_LIKE_COUNT);
        //将获取到的点赞、浏览表结构封装到文章返回列表中
        articleBackDTOList.forEach(article -> {
            Double articleViewCount = ViewMap.get(article.getId());
            if(Objects.nonNull(articleViewCount)){
                //注意double转int
                article.setViewsCount(articleViewCount.intValue());
            }
            //由于redisMap序列化结构的键是字符串，需要将id转为string
            Integer articleLikeCount =(Integer)likeMap.get(article.getId().toString());
            article.setLikeCount(articleLikeCount);
        });
        return new PageResult<>(articleBackDTOList,articleNumber);
    }

    /**
     * 注意这是逻辑删除，所以采用的是更新操作，相当于放置到垃圾桶中可恢复
     * @param deleteVO
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateArticleDelete(DeleteVO deleteVO) {
        //根据前端返回的idlist封装成新的文章对象
        List<Article> articleList = deleteVO.getIdList().stream()
                .map(id -> Article.builder()
                        .id(id)
                        .isTop(FALSE)
                        .isDelete(deleteVO.getIsDelete())
                        .build())
                .collect(Collectors.toList());
        //利用mybatis-plus的更新功能，将id相同的文章的部分属性值替换，实现逻辑删除
        this.updateBatchById(articleList);
    }

    @Override
    public void deleteArticles(List<Integer> articleIdList) {
        //删除被删除文章与标签的关联
        articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>()
        .in(ArticleTag::getArticleId,articleIdList));
        //数据库删除文章
        articleMapper.deleteBatchIds(articleIdList);
    }

    @Override
    public List<ArticleHomeDTO> listArticles() {
        return articleMapper.listArticles(PageUtils.getLimitCurrent(),PageUtils.getSize());
    }

    @Override
    public ArticleDTO getArticleById(Integer articleId) {
        ArticleDTO article = articleMapper.getArticleById(articleId);
        if(Objects.isNull(article)){
            throw new BizException("文章不存在");
        }
        Article lastArticle = articleMapper.selectOne(new LambdaQueryWrapper<Article>()
                .select(Article::getId, Article::getArticleTitle, Article::getArticleCover)
                .eq(Article::getIsDelete, FALSE)
                .eq(Article::getStatus, ArticleStatusEnum.PUBLIC.getStatus())
                .lt(Article::getId, articleId)
                .orderByDesc(Article::getId)
                .last("limit 1"));
        Article nextArticle = articleMapper.selectOne(new LambdaQueryWrapper<Article>()
                .select(Article::getId, Article::getArticleTitle, Article::getArticleCover)
                .eq(Article::getIsDelete, FALSE)
                .eq(Article::getStatus, ArticleStatusEnum.PUBLIC.getStatus())
                .gt(Article::getId, articleId)
                .orderByAsc(Article::getId)
                .last("limit 1"));

        article.setLastArticle(BeanCopyUtils.copyObject(lastArticle, ArticlePaginationDTO.class));
        article.setNextArticle(BeanCopyUtils.copyObject(nextArticle,ArticlePaginationDTO.class));
        //先更新文章的浏览量(根据ip增加)
        updateArticleViewsCount(articleId);
        //从redis中获取到文章浏览量表
        Double articleViewCount = redisUtils.zScore(RedisPrefixConst.ARTICLE_VIEWS_COUNT,articleId);
        if(Objects.nonNull(articleViewCount)){
            article.setViewsCount(articleViewCount.intValue());
        }
        //从redis中获取到点赞量表
        Integer articleLikeCount =(Integer) redisUtils.hGet(RedisPrefixConst.ARTICLE_LIKE_COUNT, articleId.toString());
        article.setLikeCount(articleLikeCount);
        //以下数据暂时不处理，后期增加
        article.setRecommendArticleList(null);
        article.setNewestArticleList(null);
        return article;
    }

    @Override
    public ArticlePreviewListDTO listArticlesByCondition(ConditionVo conditionVo) {
        //查询文章
        List<ArticlePreviewDTO> articlePreviewListDTO = articleMapper.listArticlesByCondition(PageUtils.getLimitCurrent(), PageUtils.getSize(), conditionVo);
        //若条件中有categoryId,则查询分类id对应的分类名
        String name;
        if(Objects.nonNull(conditionVo.getCategoryId())){
            name = categoryMapper.selectOne(new LambdaQueryWrapper<Category>().select(Category::getCategoryName)
                    .eq(Category::getId, conditionVo.getCategoryId())).getCategoryName();
        }else{
            name = tagMapper.selectOne(new LambdaQueryWrapper<Tag>().select(Tag::getId)
                    .eq(Tag::getId, conditionVo.getTagId())).getTagName();
        }
        //若无，则为标签页发送的请求，根据标签id查询对应的标签名
        //将数据封装成预览文章列表类返回
        ArticlePreviewListDTO articlePreviewList = ArticlePreviewListDTO.builder()
                .articlePreviewDTOList(articlePreviewListDTO)
                .name(name).build();
        return articlePreviewList;
    }

    @Override
    public PageResult<ArchiveDTO> listArchives(ConditionVo conditionVo) {
        Page<Article> page = (Page<Article>) PageUtils.getPage();
        Page<Article> articlePage = articleMapper.selectPage(page, new LambdaQueryWrapper<Article>()
                .select(Article::getId, Article::getArticleTitle, Article::getCreateTime)
                .orderByDesc(Article::getCreateTime)
                .eq(Article::getIsDelete, FALSE)
                .eq(Article::getStatus, ArticleStatusEnum.PUBLIC.getStatus()));
        List<ArchiveDTO> archiveDTOList = BeanCopyUtils.copyList(articlePage.getRecords(), ArchiveDTO.class);
        return new PageResult<>(archiveDTOList,articlePage.getRecords().size());
    }

    @Override
    public void saveArticleLike(Integer articleId) {
        String articleLikeKey = RedisPrefixConst.ARTICLE_USER_LIKE + UserUtils.getLoginUser().getUserInfoId();
        //判断当前文章是否已经存在该用户的key-value对，若无将该键值对添加到redis中，并更新浏览量
        if(redisUtils.sIsMember(articleLikeKey,articleId)){
            //若已经存在该键值对，表示已经点过赞，再次点击表示取消点赞，则删除该键值对，浏览量-1；
            redisUtils.sRemove(articleLikeKey,articleId);
            redisUtils.hDecr(RedisPrefixConst.ARTICLE_LIKE_COUNT,articleId.toString(),1L);
        }else {
            redisUtils.sAdd(articleLikeKey,articleId);
            redisUtils.hIncr(articleLikeKey,articleId.toString(),1L);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateArticleTop(ArticleTopVO articleTopVO) {
//        Article article = articleMapper.selectById(articleTopVO.getId());
//        if(Objects.isNull(article)){
//            throw new BizException("文章不存在");
//        }
//        article.setIsTop(articleTopVO.getIsTop());
//        this.saveOrUpdate(article);
        //由于实际性能上最好减少对数据库的访问，所以不从数据库中查了，直接构造一个对象，利用对象去更新，减少一次访问数据库操作
        Article article = Article.builder().id(articleTopVO.getId()).isTop(articleTopVO.getIsTop()).build();
        articleMapper.updateById(article);
    }

    /**
     * 查询该文章类型是否存在，存在则返回该类型的对应信息
     * 不存在则新建个类型并返回。
     * @param articleVO
     * @return
     */
    public Category saveArticleCategory(ArticleVO articleVO){
        Category category = categoryMapper.selectOne(new LambdaQueryWrapper<Category>()
                .eq(Category::getCategoryName, articleVO.getCategoryName()));
        //如果数据库中尚无该类型，则新建(注意前提必须是非草稿类文章)
        if(Objects.isNull(category) && !articleVO.getStatus().equals(ArticleStatusEnum.DRAFT.getStatus())){
            category = Category.builder()
                    .categoryName(articleVO.getCategoryName())
                    .build();
            //新建完后要添加到数据库中
            categoryMapper.insert(category);
        }
        return category;
    }

    /**
     * 查询该文章的标签是否存在，存在则直接将标签与文章绑定，不存在则新建后再绑定
     * @param articleVO
     * @param articleID
     * @return
     */
    public void saveArticleTag(ArticleVO articleVO, Integer articleID){
        //编辑文章则删除文章的所有已有标签
        if(Objects.nonNull(articleVO.getId())){
            articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>()
                    .eq(ArticleTag::getArticleId, articleVO.getId()));
        }
        //添加文章标签1、获得前端提交的标签名列表，2、查询已经存在的标签，3、对不存在的标签进行新增
        List<String> tagNameList = articleVO.getTagNameList();
        if(CollectionUtils.isNotEmpty(tagNameList)){
            //将数据库中的标签名与前端返回的进行比较，将出现在前段提交的标签以集合表返回，再分别获取已经存在的标签的Name和Id集合
            List<Tag> existTagList = tagService.list(new LambdaQueryWrapper<Tag>()
                    .in(Tag::getTagName, tagNameList));
            List<String> existTagNameList = existTagList.stream()
                    .map(Tag::getTagName).collect(Collectors.toList());
            List<Integer> existTagIdList = existTagList.stream()
                    .map(Tag::getId).collect(Collectors.toList());
            //删除已经存在的标签名，方便后续对不存在的标签名进行新增
            tagNameList.removeAll(existTagNameList);
            //如果标签名集合并不为空，则进行新增
            if(CollectionUtils.isNotEmpty(tagNameList)){
                //构造标签实例集合
                List<Tag> tagList = tagNameList.stream().map(item -> Tag.builder()
                        .tagName(item)
                        .build()).collect(Collectors.toList());
                tagService.saveBatch(tagList);
                List<Integer> tagIdList = tagList.stream()
                        .map(Tag::getId).collect(Collectors.toList());
                existTagIdList.addAll(tagIdList);
            }
            //完成新建标签后进行标签与文章的绑定(就是新建ArticleTag类)
            List<ArticleTag> articleTagList = existTagIdList.stream().map(item -> ArticleTag.builder()
                    .tagId(item)
                    .articleId(articleID)
                    .build()).collect(Collectors.toList());
            //调用对应接口方法完成操作数据库实现绑定
            articleTagService.saveBatch(articleTagList);
        }
    }

    /**
     * 更新文章浏览量
     * @param articleId
     */
    //@Async注解是使用线程池进行异步操作，因为更新浏览数必须是异步的操作，否则会造成数据脏写
    @Async
    public void updateArticleViewsCount(Integer articleId){
        //判断ip是否是第一次访问该文章(利用optional.ofNullable判断会话中是否已经存在文章表，没有就新建)
        Set<Integer> articleSet =(Set<Integer>) Optional.ofNullable(session.getAttribute(ARTICLE_SET)).orElse(new HashSet<>());
        //如果表中没有当前文章id，在当前会话中添加文章表属性
        if(!articleSet.contains(articleId)){
            articleSet.add(articleId);
            session.setAttribute(ARTICLE_SET,articleSet);
            //同时在redis中对以该文章id为key的value进行自增
            // （注意由于限制ip，一般的情况下只要session中防止的属性中存在文章表，
            // 且文章表内存在该文章id则相当于当前ip已经访问过）
            //但如果要进一步限制用户自行删除session中的属性导致ip刷浏览量，需要利用ipUtil工具获取设备的ip进行限制
            redisUtils.zIncr(RedisPrefixConst.ARTICLE_VIEWS_COUNT,articleId,1D);
        }

    }
}
