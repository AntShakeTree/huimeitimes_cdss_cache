package com.hm.cache;

import java.util.*;

/**
 * Created by ant_shake_tree on 15/11/17.
 */
public class Comparetors  {


    public static void sorts(TreeSet set ,Object o,double score){
        set.add(new ComparetorObject(o,score));
    }


    public static class ComparetorObject implements Comparable<ComparetorObject>{
        private Object object;
        private double score;

        public Object getObject() {
            return object;
        }

        public void setObject(Object object) {
            this.object = object;
        }

        public ComparetorObject(Object object, double score) {
            this.object = object;
            this.score = score;
        }

        public double getScore() {
            return score;
        }

        public void setScore(double score) {
            this.score = score;
        }



        @Override
        public int compareTo(ComparetorObject o) {
            return(int)(this.getScore()-o.getScore());
        }
    }
}
