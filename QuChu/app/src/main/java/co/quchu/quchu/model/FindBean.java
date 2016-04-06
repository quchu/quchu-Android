package co.quchu.quchu.model;

import java.util.List;

/**
 * User: Chenhs
 * Date: 2015-12-28
 */
public class FindBean {

    /**
     * pageCount : 1
     * pagesNo : 1
     * result : [{"address":"fdsfds","image":[{"imgpath":"http://7xo7et.com1.z0.glb.clouddn.com/123457609?imageMogr2/format/webp"}],"instruction":"fdsfdsf","name":"fdsfds","pId":12}]
     */

    private int pageCount;
    private int pagesNo;
    /**
     * address : fdsfds
     * image : [{"imgpath":"http://7xo7et.com1.z0.glb.clouddn.com/123457609?imageMogr2/format/webp"}]
     * instruction : fdsfdsf
     * name : fdsfds
     * pId : 12
     */

    private List<ResultEntity> result;

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public void setPagesNo(int pagesNo) {
        this.pagesNo = pagesNo;
    }

    public void setResult(List<ResultEntity> result) {
        this.result = result;
    }

    public int getPageCount() {
        return pageCount;
    }

    public int getPagesNo() {
        return pagesNo;
    }

    public List<ResultEntity> getResult() {
        return result;
    }

    public static class ResultEntity {
        private String address;
        private String instruction;
        private String name;
        private int pId;
        /**
         * imgpath : http://7xo7et.com1.z0.glb.clouddn.com/123457609?imageMogr2/format/webp
         */

        private List<ImageEntity> image;

        public void setAddress(String address) {
            this.address = address;
        }

        public void setInstruction(String instruction) {
            this.instruction = instruction;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPId(int pId) {
            this.pId = pId;
        }

        public void setImage(List<ImageEntity> image) {
            this.image = image;
        }

        public String getAddress() {
            return address;
        }

        public String getInstruction() {
            return instruction;
        }

        public String getName() {
            return name;
        }

        public int getPId() {
            return pId;
        }

        public List<ImageEntity> getImage() {
            return image;
        }

        public static class ImageEntity {
            private String imgpath;

            public void setImgpath(String imgpath) {
                this.imgpath = imgpath;
            }

            public String getImgpath() {
                return imgpath;
            }
        }
    }
}
