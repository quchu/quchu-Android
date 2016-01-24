package co.quchu.quchu.model;

import java.util.List;

/**
 * FavoriteModel
 * User: Chenhs
 * Date: 2015-12-14
 */
public class FavoriteModel {

    /**
     * count : 29
     * data : [{"cover":"http://7xodsq.com1.z0.glb.clouddn.com/88-default-place-cover","id":151,"name":"Love it","rgb":"635031","score":5}]
     */

    private CardEntity card;
    /**
     * count : 4
     * data : [{"cover":"http://7xodsq.com1.z0.glb.clouddn.com/88-default-place-cover","id":88,"name":"古山料理","reg":"635031","score":4}]
     */

    private PlaceEntity place;

    public void setCard(CardEntity card) {
        this.card = card;
    }

    public void setPlace(PlaceEntity place) {
        this.place = place;
    }

    public CardEntity getCard() {
        return card;
    }

    public PlaceEntity getPlace() {
        return place;
    }

    public static class CardEntity {
        private int count;
        /**
         * cover : http://7xodsq.com1.z0.glb.clouddn.com/88-default-place-cover
         * id : 151
         * name : Love it
         * rgb : 635031
         * score : 5
         */

        private List<DataEntity> data;

        public void setCount(int count) {
            this.count = count;
        }

        public void setData(List<DataEntity> data) {
            this.data = data;
        }

        public int getCount() {
            return count;
        }

        public List<DataEntity> getData() {
            return data;
        }

        public static class DataEntity {
            private String cover;
            private int id;
            private String name;
            private String rgb;
            private float score;

            public void setCover(String cover) {
                this.cover = cover;
            }

            public void setId(int id) {
                this.id = id;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setRgb(String rgb) {
                this.rgb = rgb;
            }

            public void setScore(float score) {
                this.score = score;
            }

            public String getCover() {
                return cover;
            }

            public int getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public String getRgb() {
                return rgb;
            }

            public float getScore() {
                return score;
            }
        }
    }

    public static class PlaceEntity {
        private int count;
        /**
         * cover : http://7xodsq.com1.z0.glb.clouddn.com/88-default-place-cover
         * id : 88
         * name : 古山料理
         * reg : 635031
         * score : 4
         */

        private List<DataEntity> data;

        public void setCount(int count) {
            this.count = count;
        }

        public void setData(List<DataEntity> data) {
            this.data = data;
        }

        public int getCount() {
            return count;
        }

        public List<DataEntity> getData() {
            return data;
        }

        public static class DataEntity {
            private String cover;
            private int id;
            private String name;
            private String rgb;
            private int score;

            public void setCover(String cover) {
                this.cover = cover;
            }

            public void setId(int id) {
                this.id = id;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setRgb(String reg) {
                this.rgb = reg;
            }

            public void setScore(int score) {
                this.score = score;
            }

            public String getCover() {
                return cover;
            }

            public int getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public String getRgb() {
                return rgb;
            }

            public int getScore() {
                return score;
            }
        }
    }
}
