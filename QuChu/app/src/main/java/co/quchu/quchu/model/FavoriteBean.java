package co.quchu.quchu.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by no21 on 2016/4/5.
 * email:437943145@qq.com
 * desc : 趣处里面 发现和收藏通用实体类
 */
public class FavoriteBean {


    /**
     * pageCount : 1
     * pageSize : 10
     * pagesNo : 1
     * result : [{"address":"内厝澳路459号","autor":"趣处","autorId":"2","autorPhoto":"http://7xo7et.com1.z0.glb.clouddn.com/app-default-avatar-9","cover":"http://7xodsq.com1.z0.glb.clouddn.com/357-default-app-place-cover?imageMogr2/thumbnail/800x/format/webp","describe":"厦门,鼓浪屿风景区","height":480,"isf":false,"latitude":"24.455591654875","longitude":"118.07262540828","name":"厦门鼓浪屿留下精品客栈","pid":357,"rgb":"707f66","suggest":5,"tags":[{"zh":"花园别墅","en":null,"code":null,"tagId":204}],"width":720},{"address":"鼓浪屿龙头路菜市场二楼","autor":"趣处","autorId":"2","autorPhoto":"http://7xo7et.com1.z0.glb.clouddn.com/app-default-avatar-9","cover":"http://7xodsq.com1.z0.glb.clouddn.com/352-default-app-place-cover?imageMogr2/thumbnail/800x/format/webp","describe":"厦门,鼓浪屿风景区","height":451,"isf":false,"latitude":"24.452339588489","longitude":"118.07367153985","name":"菜市场博物馆","pid":352,"rgb":"583f2f","suggest":4.5,"tags":[{"zh":"闽菜","en":null,"code":null,"tagId":193},{"zh":"海鲜","en":null,"code":null,"tagId":194}],"width":675},{"address":"轮渡和平码头内一号栈桥","autor":"趣处","autorId":"2","autorPhoto":"http://7xo7et.com1.z0.glb.clouddn.com/app-default-avatar-9","cover":"http://7xodsq.com1.z0.glb.clouddn.com/364-default-app-place-cover?imageMogr2/thumbnail/800x/format/webp","describe":"厦门,中山路/轮渡","height":580,"isf":false,"latitude":"24.455757095145","longitude":"118.08387771874","name":"太鼓壹号","pid":364,"rgb":"7f6845","suggest":4,"tags":[{"zh":"海景","en":null,"code":null,"tagId":192}],"width":871},{"address":"思明南路127中华城1F33-34号","autor":"趣处","autorId":"2","autorPhoto":"http://7xo7et.com1.z0.glb.clouddn.com/app-default-avatar-9","cover":"http://7xodsq.com1.z0.glb.clouddn.com/376-default-app-place-cover?imageMogr2/thumbnail/800x/format/webp","describe":"厦门,中山路/轮渡","height":479,"isf":false,"latitude":"24.458156617514","longitude":"118.08981331013","name":"EO咖餐(中华城店)","pid":376,"rgb":"7e6b43","suggest":4,"tags":[{"zh":"西餐","en":null,"code":null,"tagId":191}],"width":718},{"address":"菜妈街4号建设大厦旁","autor":"趣处","autorId":"2","autorPhoto":"http://7xo7et.com1.z0.glb.clouddn.com/app-default-avatar-9","cover":"http://7xodsq.com1.z0.glb.clouddn.com/368-default-app-place-cover?imageMogr2/thumbnail/800x/format/webp","describe":"厦门,中山路/轮渡","height":390,"isf":false,"latitude":"24.465840287157","longitude":"118.09105556873","name":"厦门原至花园别墅工业复式阁楼房","pid":368,"rgb":"8a847c","suggest":3.5,"tags":[{"zh":"别墅","en":null,"code":null,"tagId":216}],"width":585},{"address":"滨湖北路60-80号阿罗海购物广场","autor":"趣处","autorId":"2","autorPhoto":"http://7xo7et.com1.z0.glb.clouddn.com/app-default-avatar-9","cover":"http://7xodsq.com1.z0.glb.clouddn.com/389-default-app-place-cover?imageMogr2/thumbnail/800x/format/webp","describe":"厦门,阿罗海城市广场","height":457,"isf":false,"latitude":"24.485162665399","longitude":"118.03803057957","name":"纸的时代书店","pid":389,"rgb":"a68c76","suggest":4.5,"tags":[{"zh":"厦门最大的书店","en":null,"code":null,"tagId":231}],"width":686},{"address":"乌埭路2号","autor":"趣处","autorId":"2","autorPhoto":"http://7xo7et.com1.z0.glb.clouddn.com/app-default-avatar-9","cover":"http://7xodsq.com1.z0.glb.clouddn.com/388-default-app-place-cover?imageMogr2/thumbnail/800x/format/webp","describe":"厦门,鼓浪屿风景区","height":397,"isf":false,"latitude":"24.450962577087","longitude":"118.07535979546","name":"海怪先生","pid":388,"rgb":"6f756f","suggest":4,"tags":[{"zh":"可爱","en":null,"code":null,"tagId":212},{"zh":"卡通","en":null,"code":null,"tagId":230}],"width":596},{"address":"思明西路51-53号南中广场中山路口大门2楼","autor":"趣处","autorId":"2","autorPhoto":"http://7xo7et.com1.z0.glb.clouddn.com/app-default-avatar-9","cover":"http://7xodsq.com1.z0.glb.clouddn.com/392-default-app-place-cover?imageMogr2/thumbnail/800x/format/webp","describe":"厦门,中山路/轮渡","height":400,"isf":false,"latitude":"24.460482255042","longitude":"118.08494472423","name":"港记茶餐厅","pid":392,"rgb":"7f7e76","suggest":4.5,"tags":[{"zh":"港式茶餐厅","en":null,"code":null,"tagId":234}],"width":600},{"address":"玄武大道96号聚宝山公园","autor":"趣处","autorId":"2","autorPhoto":"http://7xo7et.com1.z0.glb.clouddn.com/app-default-avatar-9","cover":"http://7xodsq.com1.z0.glb.clouddn.com/480-default-app-place-cover?imageMogr2/thumbnail/800x/format/webp","describe":"南京,仙鹤门新天地广场","height":400,"isf":false,"latitude":"32.112052940138","longitude":"118.88053540668","name":"Meet Park公园别墅聚会俱乐部","pid":480,"rgb":"6d6c5e","suggest":5,"tags":[{"zh":"聚会","en":null,"code":null,"tagId":258}],"width":600},{"address":"湖滨西路21-5","autor":"趣处","autorId":"2","autorPhoto":"http://7xo7et.com1.z0.glb.clouddn.com/app-default-avatar-9","cover":"http://7xodsq.com1.z0.glb.clouddn.com/385-default-app-place-cover?imageMogr2/thumbnail/800x/format/webp","describe":"厦门,禾祥西路","height":0,"isf":false,"latitude":"24.469419520722","longitude":"118.08460176556","name":"Hello KD Café","pid":385,"rgb":"909090","suggest":4.5,"tags":[{"zh":"hellokitty","en":null,"code":null,"tagId":228}],"width":0}]
     * resultCount : 10
     * rowCount : 10
     * rowEnd : 10
     * rowStart : 0
     */

    private int pageCount;
    private int pageSize;
    private int pagesNo;
    private int resultCount;
    private int rowCount;
    private int rowEnd;
    private int rowStart;
    /**
     * address : 内厝澳路459号
     * autor : 趣处
     * autorId : 2
     * autorPhoto : http://7xo7et.com1.z0.glb.clouddn.com/app-default-avatar-9
     * cover : http://7xodsq.com1.z0.glb.clouddn.com/357-default-app-place-cover?imageMogr2/thumbnail/800x/format/webp
     * describe : 厦门,鼓浪屿风景区
     * height : 480
     * isf : false
     * latitude : 24.455591654875
     * longitude : 118.07262540828
     * name : 厦门鼓浪屿留下精品客栈
     * pid : 357
     * rgb : 707f66
     * suggest : 5
     * tags : [{"zh":"花园别墅","en":null,"code":null,"tagId":204}]
     * width : 720
     */

    private List<ResultBean> result;

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPagesNo() {
        return pagesNo;
    }

    public void setPagesNo(int pagesNo) {
        this.pagesNo = pagesNo;
    }

    public int getResultCount() {
        return resultCount;
    }

    public void setResultCount(int resultCount) {
        this.resultCount = resultCount;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getRowEnd() {
        return rowEnd;
    }

    public void setRowEnd(int rowEnd) {
        this.rowEnd = rowEnd;
    }

    public int getRowStart() {
        return rowStart;
    }

    public void setRowStart(int rowStart) {
        this.rowStart = rowStart;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        private String address;
        private String autor;
        private String autorId;
        private String autorPhoto;
        private String cover;
        private String describe;
        private int height;
        private boolean isf;
        private String latitude;
        private String longitude;
        private String name;
        private int pid;
        private String rgb;
        private float suggest;
        private int width;
        /**
         * zh : 花园别墅
         * en : null
         * code : null
         * tagId : 204
         */

        private List<TagsBean> tags;

        public List<String> getTagsString() {
            ArrayList<String> list = new ArrayList<>();
            if (tags != null) {
                for (TagsBean item : tags) {
                    list.add(item.getZh());
                }
            }
            return list;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAutor() {
            return autor;
        }

        public void setAutor(String autor) {
            this.autor = autor;
        }

        public String getAutorId() {
            return autorId;
        }

        public void setAutorId(String autorId) {
            this.autorId = autorId;
        }

        public String getAutorPhoto() {
            return autorPhoto;
        }

        public void setAutorPhoto(String autorPhoto) {
            this.autorPhoto = autorPhoto;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public boolean isIsf() {
            return isf;
        }

        public void setIsf(boolean isf) {
            this.isf = isf;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public String getRgb() {
            return rgb;
        }

        public void setRgb(String rgb) {
            this.rgb = rgb;
        }

        public float getSuggest() {
            return suggest;
        }

        public void setSuggest(int suggest) {
            this.suggest = suggest;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public List<TagsBean> getTags() {
            return tags;
        }

        public void setTags(List<TagsBean> tags) {
            this.tags = tags;
        }

        public static class TagsBean {
            private String zh;
            private Object en;
            private Object code;
            private int tagId;

            public String getZh() {
                return zh;
            }

            public void setZh(String zh) {
                this.zh = zh;
            }

            public Object getEn() {
                return en;
            }

            public void setEn(Object en) {
                this.en = en;
            }

            public Object getCode() {
                return code;
            }

            public void setCode(Object code) {
                this.code = code;
            }

            public int getTagId() {
                return tagId;
            }

            public void setTagId(int tagId) {
                this.tagId = tagId;
            }
        }
    }
}
