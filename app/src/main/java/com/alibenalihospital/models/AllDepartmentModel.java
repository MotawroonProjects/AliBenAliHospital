package com.alibenalihospital.models;

import java.io.Serializable;
import java.util.List;


public class AllDepartmentModel extends StatusResponse implements Serializable  {
    public List<DepartmentData> data;

    public List<DepartmentData> getData() {
        return data;
    }

    public class DepartmentData implements Serializable{
       public int id;
       public String image;
       public String name_ar;
       public String name_en;
       public String created_at;
       public String updated_at;
       public String name;

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
}
