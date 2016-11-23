package co.quchu.quchu.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * DetailModel
 * User: Chenhs
 * Date: 2015-12-13
 */
public class DetailModel implements Serializable{

    /**
     * activityInfo : 本次活动主要针对领基础的绘画爱好者,来之前一定要先预约哦
     * activityInfoHtml : 本次活来之前一定要先预约哦
     * address : 鼓浪屿龙头路菜市场二楼
     * autor : 管理员
     * autorId : 1
     * autorPhoto : http://7xo7et.com1.z0.glb.clouddn.com/1-app-default-avatar
     * businessHours : 10:00-00:00
     * cover : http://7xo7et.com1.z0.glb.clouddn.com/5-default-app-place-cover?imageMogr2/format/webp
     * genes : [{"key":"美食","value":"79"}]
     * height : 451
     * icons : [{"zh":"现金"}]
     * imglist : [{"cid":15,"cindex":0,"height":451,"imgpath":"http://7xo7et.com1.z0.glb.clouddn.com/5@1?imageMogr2/format/webp","width":675}]
     * isActivity : true
     * isf : false
     * isout : false
     * latitude : 24.452339588489
     * longitude : 118.07367153985
     * name : 菜市场博物馆
     * net :
     * pid : 5
     * price : 64
     * restDay :
     * rgb : 583f2f
     * suggest : 5
     * tags : [{"zh":"闽菜"}]
     * tel : 0592-7701155
     * traffic : 龙头路菜市场
     * width : 675
     */

    private String areaCircleName;
    private String activityInfo;
    private String activityInfoHtml;
    private String address = "";
    private String autor;
    private String autorPhoto;
    private String businessHours = "";
    private String cover;
    private String latitude;
    private String longitude;
    private String name;
    private String net;
    private String price;
    private String described = "";
    private String restDay = "";
    private String rgb;
    private String tel;
    private String traffic = "";
    public String gdLatitude= "";
    public String gdLongitude= "";
    private String lineUrl = "";
    private String takeoutUrl = "";
    private int joinPartnerCount;
    private int placeReviewCount;
    private boolean isActivity;
    private boolean isf;
    private boolean isout;
    private boolean isMap = true;
    private int width;
    private int pid;
    private int autorId;
    private int height;
    private float suggest;
    private float recentSuggest;
    private int myCardId;                   //明信片ID -1为未创建明信片
    private List<GenesEntity> genes;
    private List<IconsEntity> icons;
    private List<ImglistEntity> imglist;
    private List<TagsEntity> tags;
    private List<NearPlace> recommendPlaces;
    private List<CommentModel> reviewList;
    private int cardCount;
    private SimpleCircleModel areaMap;
    private SimpleCircleModel circleMap;
    private List<QuchuDetailArticleModel> articleList;
    private List<String> reviewTagList;
    private List<BizInfoModel> reviewGroupList;
    private String circleName;

    public String getCircleName() {
        return circleName;
    }

    public void setCircleName(String circleName) {
        this.circleName = circleName;
    }

    public List<String> getReviewTagList() {
        return reviewTagList;
    }

    public void setReviewTagList(List<String> reviewTagList) {
        this.reviewTagList = reviewTagList;
    }

    public List<BizInfoModel> getReviewGroupList() {
        return reviewGroupList;
    }

    public void setReviewGroupList(List<BizInfoModel> reviewGroupList) {
        this.reviewGroupList = reviewGroupList;
    }

    public List<QuchuDetailArticleModel> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<QuchuDetailArticleModel> articleList) {
        this.articleList = articleList;
    }

    public float getRecentSuggest() {
        return recentSuggest;
    }

    public void setRecentSuggest(float recentSuggest) {
        this.recentSuggest = recentSuggest;
    }

    public String getDescribed() {
        return described;
    }

    public void setDescribed(String described) {
        this.described = described;
    }

    public SimpleCircleModel getAreaMap() {
        return areaMap;
    }

    public void setAreaMap(SimpleCircleModel areaMap) {
        this.areaMap = areaMap;
    }

    public SimpleCircleModel getCircleMap() {
        return circleMap;
    }

    public void setCircleMap(SimpleCircleModel circleMap) {
        this.circleMap = circleMap;
    }

    public String getAreaCircleName() {
        return areaCircleName;
    }

    public void setAreaCircleName(String areaCircleName) {
        this.areaCircleName = areaCircleName;
    }

    public int getCardCount() {
        return cardCount;
    }

    public void setCardCount(int cardCount) {
        this.cardCount = cardCount;
    }

    public List<NearPlace> getNearPlace() {
        return recommendPlaces;
    }

    public void setNearPlace(List<NearPlace> nearPlace) {
        this.recommendPlaces = nearPlace;
    }

    public int getJoinPartnerCount() {
        return joinPartnerCount;
    }

    public void setJoinPartnerCount(int joinPartnerCount) {
        this.joinPartnerCount = joinPartnerCount;
    }

    public int getPlaceReviewCount() {
        return placeReviewCount;
    }

    public void setPlaceReviewCount(int placeReviewCount) {
        this.placeReviewCount = placeReviewCount;
    }

    public NearbyMapModel convert2NearbyMapItem(){
        NearbyMapModel model = new NearbyMapModel();
        model.setAddress(this.getAddress());
        model.setCover(this.getCover());
        model.setName(this.getName());
        //model.setDescribed(this.get);
        model.setGdLatitude(this.gdLatitude);
        model.setGdLongitude(this.gdLongitude);
        model.setLatitude(this.getLatitude());
        model.setLongitude(this.getLongitude());
        model.setHeight(this.getHeight());
        model.setWidth(this.getWidth());
        model.setPid(this.getPid());

        List<TagsModel> tagModel = new ArrayList<>();
        if (null==this.getTags()){
            model.setTags(tagModel);
        }else{
            for (int i = 0; i < this.getTags().size(); i++) {
                TagsModel tag = new TagsModel();
                tag.setZh(getTags().get(i).getZh());
                tag.setTagId(getTags().get(i).getId());
                tagModel.add(tag);
            }
            model.setTags(tagModel);
        }
        return model;
    }


    public boolean isMap() {
        return isMap;
    }

    public void setMap(boolean map) {
        isMap = map;
    }

    public void copyFrom(DetailModel objTarget){

        setActivityInfo(objTarget.getActivityInfo());
        setActivityInfo(objTarget.getActivityInfo());
        setActivityInfoHtml(objTarget.getActivityInfoHtml());
        setAddress(objTarget.getAddress());
        setAutor(objTarget.getAutor());
        setAutorPhoto(objTarget.getAutorPhoto());
        setBusinessHours(objTarget.getBusinessHours());
        setCover(objTarget.getCover());
        setLatitude(objTarget.getLatitude());
        setLongitude(objTarget.getLongitude());
        setName(objTarget.getName());
        setNet(objTarget.getNet());
        setPrice(objTarget.getPrice());
        setRestDay(objTarget.getRestDay());
        setRgb(objTarget.getRgb());
        setTel(objTarget.getTel());
        setTraffic(objTarget.getTraffic());
        setIsActivity(objTarget.isIsActivity());
        setIsf(objTarget.isIsf());
        setIsout(objTarget.isIsout());
        setWidth(objTarget.getWidth());
        setPid(objTarget.getPid());
        setAutorId(objTarget.getAutorId());
        setHeight(objTarget.getHeight());
        setSuggest(objTarget.getSuggest());
        setMyCardId(objTarget.getMyCardId());
        setMap(objTarget.isMap());
        setJoinPartnerCount(objTarget.getJoinPartnerCount());
        setPlaceReviewCount(objTarget.getPlaceReviewCount());
        setAreaMap(objTarget.getAreaMap());
        setCircleMap(objTarget.getCircleMap());
        setDescribed(objTarget.getDescribed());
        setRecentSuggest(objTarget.getRecentSuggest());
        setTakeoutUrl(objTarget.getTakeoutUrl());
        setLineUrl(objTarget.getLineUrl());


        setAreaCircleName(objTarget.getAreaCircleName());
        setCircleName(objTarget.getCircleName());

        this.gdLatitude = objTarget.gdLatitude;
        this.gdLongitude = objTarget.gdLongitude;

        if (null==genes){
            genes = new ArrayList<>();
        }else{
            genes.clear();
        }
        if (objTarget.getGenes() != null) {
            genes.addAll(objTarget.getGenes());
        }

        if (null==icons){
            icons = new ArrayList<>();
        }else{
            icons.clear();
        }

        if (objTarget.getIcons() != null) {
            icons.addAll(objTarget.getIcons());
        }

        if (null==imglist){
            imglist = new ArrayList<>();
        }else{
            imglist.clear();
        }

        if (objTarget.getImglist() != null) {
            imglist.addAll(objTarget.getImglist());
        }

        if (null==tags){
            tags = new ArrayList<>();
        }else{
            tags.clear();
        }
        if (objTarget.getTags() != null) {
            tags.addAll(objTarget.getTags());
        }


        if (null==recommendPlaces){
            recommendPlaces = new ArrayList<>();
        }else{
            recommendPlaces.clear();
        }

        if (objTarget.getNearPlace() != null) {
            recommendPlaces.addAll(objTarget.getNearPlace());
        }

        if (null==reviewList){
            reviewList = new ArrayList<>();
        }
        if (objTarget.getReviewList()!=null){
            reviewList.addAll(objTarget.getReviewList());
        }

        if (null==articleList){
            articleList = new ArrayList<>();
        }
        if (null!= objTarget.getArticleList()){
            articleList.addAll(objTarget.getArticleList());
        }

        if (null==reviewTagList){
            reviewTagList = new ArrayList<>();
        }
        if (null!= objTarget.getReviewTagList()){
            reviewTagList.addAll(objTarget.getReviewTagList());
        }

        if (null==reviewGroupList){
            reviewGroupList = new ArrayList<>();
        }
        if (null!= objTarget.getReviewGroupList()){
            reviewGroupList.addAll(objTarget.getReviewGroupList());
        }


        cardCount = objTarget.getCardCount();


    }

    public String getTakeoutUrl() {
        return takeoutUrl;
    }

    public void setTakeoutUrl(String takeoutUrl) {
        this.takeoutUrl = takeoutUrl;
    }

    public String getLineUrl() {
        return lineUrl;
    }

    public void setLineUrl(String lineUrl) {
        this.lineUrl = lineUrl;
    }

    public List<CommentModel> getReviewList() {
        return reviewList;
    }

    public void setReviewList(List<CommentModel> reviewList) {
        this.reviewList = reviewList;
    }

    public void setActivityInfo(String activityInfo) {
        this.activityInfo = activityInfo;
    }

    public void setActivityInfoHtml(String activityInfoHtml) {
        this.activityInfoHtml = activityInfoHtml;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public void setAutorId(int autorId) {
        this.autorId = autorId;
    }

    public void setAutorPhoto(String autorPhoto) {
        this.autorPhoto = autorPhoto;
    }

    public void setBusinessHours(String businessHours) {
        this.businessHours = businessHours;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setIsActivity(boolean isActivity) {
        this.isActivity = isActivity;
    }

    public void setIsf(boolean isf) {
        this.isf = isf;
    }

    public void setIsout(boolean isout) {
        this.isout = isout;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNet(String net) {
        this.net = net;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setRestDay(String restDay) {
        this.restDay = restDay;
    }

    public void setRgb(String rgb) {
        this.rgb = rgb;
    }

    public void setSuggest(float suggest) {
        this.suggest = suggest;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setTraffic(String traffic) {
        this.traffic = traffic;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setGenes(List<GenesEntity> genes) {
        this.genes = genes;
    }

    public void setIcons(List<IconsEntity> icons) {
        this.icons = icons;
    }

    public void setImglist(List<ImglistEntity> imglist) {
        this.imglist = imglist;
    }

    public void setTags(List<TagsEntity> tags) {
        this.tags = tags;
    }

    public String getActivityInfo() {
        return activityInfo;
    }

    public String getActivityInfoHtml() {
        return activityInfoHtml;
    }

    public String getAddress() {
        return address;
    }

    public String getAutor() {
        return autor;
    }

    public int getAutorId() {
        return autorId;
    }

    public String getAutorPhoto() {
        return autorPhoto;
    }

    public String getBusinessHours() {
        return businessHours;
    }

    public String getCover() {
        return cover;
    }

    public int getHeight() {
        return height;
    }

    public boolean isIsActivity() {
        return isActivity;
    }

    public boolean isIsf() {
        return isf;
    }

    public boolean isIsout() {
        return isout;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

    public String getNet() {
        return net;
    }

    public int getPid() {
        return pid;
    }

    public String getPrice() {
        return price;
    }

    public String getRestDay() {
        return restDay;
    }

    public String getRgb() {
        return rgb;
    }

    public float getSuggest() {
        return suggest;
    }

    public String getTel() {
        return tel;
    }

    public String getTraffic() {
        return traffic;
    }

    public int getWidth() {
        return width;
    }

    public List<GenesEntity> getGenes() {
        return genes;
    }

    public List<IconsEntity> getIcons() {
        return icons;
    }

    public List<ImglistEntity> getImglist() {
        return imglist;
    }

    public List<TagsEntity> getTags() {
        return tags;
    }

    public static class GenesEntity implements Serializable{
        private String key;
        private String value;

        public void setKey(String key) {
            this.key = key;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }

    public static class IconsEntity implements Serializable {
        private int id;
        private String zh;

        public void setZh(String zh) {
            this.zh = zh;
        }

        public String getZh() {
            return zh;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public static class ImglistEntity implements Serializable {
        private int cid;
        private int cindex;
        private int height;
        private String imgpath;
        private int width;
        private String words;


        public ImageModel convert2ImageModel(){
            ImageModel imageModel = new ImageModel();
            imageModel.setPath(imgpath);
            imageModel.setImgId(cid);
            imageModel.setHeight(height);
            imageModel.setWidth(width);
            return imageModel;
        }

        public String getWords() {
            return words;
        }

        public void setWords(String words) {
            this.words = words;
        }

        public void setCid(int cid) {
            this.cid = cid;
        }

        public void setCindex(int cindex) {
            this.cindex = cindex;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public void setImgpath(String imgpath) {
            this.imgpath = imgpath;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getCid() {
            return cid;
        }

        public int getCindex() {
            return cindex;
        }

        public int getHeight() {
            return height;
        }

        public String getImgpath() {
            return imgpath;
        }

        public int getWidth() {
            return width;
        }
    }

    public static class TagsEntity  implements Serializable{
        private String zh;

        public void setZh(String zh) {
            this.zh = zh;
        }

        public String getZh() {
            return zh;
        }

        public int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        private int tagId;

        public int getTagId() {
            return tagId;
        }

        public void setTagId(int tagId) {
            this.tagId = tagId;
        }
    }

    public static class NearPlace implements Serializable{
        private int pid;
        private String cover;
        private String name;
        private String address;
        private boolean isActivity;
        private String circleName;
        private String areaCircleName;
        private List<TagsModel> tags;

        public String getAreaCircleName() {
            return areaCircleName;
        }

        public void setAreaCircleName(String areaCircleName) {
            this.areaCircleName = areaCircleName;
        }

        public String getCircleName() {
            return circleName;
        }

        public void setCircleName(String circleName) {
            this.circleName = circleName;
        }

        public int getPlaceId() {
            return pid;
        }

        public void setPlaceId(int placeId) {
            this.pid = placeId;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public boolean isActivity() {
            return isActivity;
        }

        public void setActivity(boolean activity) {
            isActivity = activity;
        }

        public List<TagsModel> getTags() {
            return tags;
        }

        public void setTags(List<TagsModel> tags) {
            this.tags = tags;
        }
    }

    public static class BizInfoModel implements Serializable{
        private float score;
        private int type;
        private String sourceName;
        private String sourceImgUrl;

        public float getScore() {
            return score;
        }

        public void setScore(float score) {
            this.score = score;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getSourceName() {
            return sourceName;
        }

        public void setSourceName(String sourceName) {
            this.sourceName = sourceName;
        }

        public String getSourceImgUrl() {
            return sourceImgUrl;
        }

        public void setSourceImgUrl(String sourceImgUrl) {
            this.sourceImgUrl = sourceImgUrl;
        }
    }

    public int getMyCardId() {
        return myCardId;
    }

    public void setMyCardId(int myCardId) {
        this.myCardId = myCardId;
    }
}
