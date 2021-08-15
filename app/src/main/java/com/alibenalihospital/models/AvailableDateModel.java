package com.alibenalihospital.models;

import java.io.Serializable;
import java.util.List;

public class AvailableDateModel implements Serializable {
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

        public static class AvailableHour implements Serializable{
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
