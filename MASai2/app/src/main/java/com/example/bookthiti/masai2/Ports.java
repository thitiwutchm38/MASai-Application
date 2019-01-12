package com.example.bookthiti.masai2;

import java.util.Comparator;

public class Ports {


    private String p_number;
    private String p_name;
    private String p_protocal;



    public void setP_number(String p_number) {
        this.p_number = p_number;
    }

    public String getP_number() {
        return p_number;
    }



    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public String getP_name() {
        return p_name;
    }


    public void setP_protocal(String p_protocal) {
        this.p_protocal = p_protocal;
    }

    public String getP_protocal() {
        return p_protocal;
    }






    public static Comparator<Ports> portNum_asc = new Comparator<Ports>() {

        public int compare(Ports s1, Ports s2) {

            int rollno1 = Integer.parseInt(s1.getP_number());
            int rollno2 = Integer.parseInt(s2.getP_number());

            /*For ascending order*/
            return rollno2-rollno1;

        }};
    public static Comparator<Ports> portNum_des = new Comparator<Ports>() {

        public int compare(Ports s1, Ports s2) {

            int rollno1 = Integer.parseInt(s1.getP_number());
            int rollno2 = Integer.parseInt(s2.getP_number());


            /*For descending order*/
            return rollno1-rollno2;
        }};




    public static Comparator<Ports> portsName_asc = new Comparator<Ports>() {

        public int compare(Ports s1, Ports s2) {


            String c1 = s1.getP_name();
            String c2 = s2.getP_name();



            if (c1.toLowerCase().equals(s1) && c2.toUpperCase().equals(s2)) {
                return 1;
            }

            if (c1.toUpperCase().equals(s1) && c2.toLowerCase().equals(s2)) {
                return -1;
            }

            return c1.compareTo(c2);

        }};
    public static Comparator<Ports> portsName_des = new Comparator<Ports>() {

        public int compare(Ports s1, Ports s2) {


            String c1 = s1.getP_name();
            String c2 = s2.getP_name();



            if (c1.toLowerCase().equals(s1) && c2.toUpperCase().equals(s2)) {
                return 1;
            }

            if (c1.toUpperCase().equals(s1) && c2.toLowerCase().equals(s2)) {
                return -1;
            }

            return c2.compareTo(c1);

        }};





    public static Comparator<Ports> portsProtocal_asc = new Comparator<Ports>() {

        public int compare(Ports s1, Ports s2) {


            String c1 = s1.getP_protocal();
            String c2 = s2.getP_protocal();



            if (c1.toLowerCase().equals(s1) && c2.toUpperCase().equals(s2)) {
                return 1;
            }

            if (c1.toUpperCase().equals(s1) && c2.toLowerCase().equals(s2)) {
                return -1;
            }

            return c1.compareTo(c2);

        }};
    public static Comparator<Ports> portsProtocal_des = new Comparator<Ports>() {

        public int compare(Ports s1, Ports s2) {


            String c1 = s1.getP_protocal();
            String c2 = s2.getP_protocal();



            if (c1.toLowerCase().equals(s1) && c2.toUpperCase().equals(s2)) {
                return 1;
            }

            if (c1.toUpperCase().equals(s1) && c2.toLowerCase().equals(s2)) {
                return -1;
            }

            return c2.compareTo(c1);

        }};



}