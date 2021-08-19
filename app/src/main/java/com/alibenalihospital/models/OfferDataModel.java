package com.alibenalihospital.models;

import java.io.Serializable;
import java.util.List;

public class OfferDataModel extends StatusResponse implements Serializable {
    private OfferData data;

    public OfferData getData() {
        return data;
    }

    public class OfferData implements Serializable{
        private int id;
        private String doctor_id;
        private int clinic_id;
        private int offer;
        private String old_price;
        private String new_price;
        private double rate;
        private String title_ar;
        private String title_en;
        private String details_ar;
        private String details_en;
        private String created_at;
        private String  updated_at;
        private String title;
        private String details;
        private List<SliderModel> images;
        private Clinic clinic;
        private FirstImage first_image;
        private List<RateModel> rates;
        private List<DateModel> available_date;
        private List<DateModel> all_dates;

        public int getId() {
            return id;
        }

        public String getDoctor_id() {
            return doctor_id;
        }

        public int getClinic_id() {
            return clinic_id;
        }

        public int getOffer() {
            return offer;
        }

        public String getOld_price() {
            return old_price;
        }

        public String getNew_price() {
            return new_price;
        }

        public double getRate() {
            return rate;
        }

        public String getTitle_ar() {
            return title_ar;
        }

        public String getTitle_en() {
            return title_en;
        }

        public String getDetails_ar() {
            return details_ar;
        }

        public String getDetails_en() {
            return details_en;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public String getTitle() {
            return title;
        }

        public String getDetails() {
            return details;
        }

        public List<SliderModel> getImages() {
            return images;
        }

        public Clinic getClinic() {
            return clinic;
        }

        public FirstImage getFirst_image() {
            return first_image;
        }

        public List<RateModel> getRates() {
            return rates;
        }

        public List<DateModel> getAvailable_date() {
            return available_date;
        }

        public List<DateModel> getAll_dates() {
            return all_dates;
        }

        public class Clinic implements Serializable{
            private int id;
            private String image;
            private String name_ar;
            private String name_en;
            private String created_at;
            private String updated_at;
            private String name;

            public int getId() {
                return id;
            }

            public String getImage() {
                return image;
            }

            public String getName_ar() {
                return name_ar;
            }

            public String getName_en() {
                return name_en;
            }

            public String getCreated_at() {
                return created_at;
            }

            public String getUpdated_at() {
                return updated_at;
            }

            public String getName() {
                return name;
            }
        }

        public class FirstImage implements Serializable{
            private int id;
            private int offer_id;
            private String image;
            private String created_at;
            private String updated_at;

            public int getId() {
                return id;
            }

            public int getOffer_id() {
                return offer_id;
            }

            public String getImage() {
                return image;
            }

            public String getCreated_at() {
                return created_at;
            }

            public String getUpdated_at() {
                return updated_at;
            }
        }






    }

}
