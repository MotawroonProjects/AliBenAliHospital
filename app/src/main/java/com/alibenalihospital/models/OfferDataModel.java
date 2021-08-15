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
        private double old_price;
        private double new_price;
        private double rate;
        private String title_ar;
        private String title_en;
        private String details_ar;
        private String details_en;
        private String created_at;
        private String  updated_at;
        private String title;
        private String details;
        private List<Images> images;
        private Clinic clinic;
        private FirstImage first_image;
        private List<Rate> rates;
        private List<AvailableDate> available_date;

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

        public double getOld_price() {
            return old_price;
        }

        public double getNew_price() {
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

        public List<Images> getImages() {
            return images;
        }

        public Clinic getClinic() {
            return clinic;
        }

        public FirstImage getFirst_image() {
            return first_image;
        }

        public List<Rate> getRates() {
            return rates;
        }

        public List<AvailableDate> getAvailable_date() {
            return available_date;
        }

        public class Images implements Serializable{
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

        public class Clinic{
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

        public class FirstImage{
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



        public class Rate{
            private int id;
            private int rate;
            private String doctor_id;
            private int offer_id;
            private int user_id;
            private String title;
            private String created_at;
            private String updated_at;
            private UserModel.User user;

            public int getId() {
                return id;
            }

            public int getRate() {
                return rate;
            }

            public String getDoctor_id() {
                return doctor_id;
            }

            public int getOffer_id() {
                return offer_id;
            }

            public int getUser_id() {
                return user_id;
            }

            public String getTitle() {
                return title;
            }

            public String getCreated_at() {
                return created_at;
            }

            public String getUpdated_at() {
                return updated_at;
            }

            public UserModel.User getUser() {
                return user;
            }
        }


        public class AvailableDate{
            private int id;
            private String doctor_id;
            private int offer_id;
            private String date;
            private String is_reserved;
            private String created_at;
            private String updated_at;
            private List<AvailableHour> available_hour;

            public int getId() {
                return id;
            }

            public String getDoctor_id() {
                return doctor_id;
            }

            public int getOffer_id() {
                return offer_id;
            }

            public String getDate() {
                return date;
            }

            public String getIs_reserved() {
                return is_reserved;
            }

            public String getCreated_at() {
                return created_at;
            }

            public String getUpdated_at() {
                return updated_at;
            }

            public List<AvailableHour> getAvailable_hour() {
                return available_hour;
            }

            public class AvailableHour{
                private int id;
                private int date_id;
                private String hour;
                private String is_reserved;
                private String created_at;
                private String updated_at;

                public int getId() {
                    return id;
                }

                public int getDate_id() {
                    return date_id;
                }

                public String getHour() {
                    return hour;
                }

                public String getIs_reserved() {
                    return is_reserved;
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

}