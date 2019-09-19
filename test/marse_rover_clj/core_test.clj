(ns marse-rover-clj.core-test
  (:require [clojure.test :refer [deftest is run-tests]]
            [marse-rover-clj.core :refer [process-instruction]]))

(deftest robot-turning-left-cicle
  (is (= [{:robot {:orientation \N}}
          {:robot {:orientation \W}}
          {:robot {:orientation \S}}
          {:robot {:orientation \E}}
          {:robot {:orientation \N}}]
         (reductions process-instruction
                     {:robot {:orientation \N}}
                     (seq "LLLL")))))

(deftest robot-turning-right-cicle
  (is (= [{:robot {:orientation \N}}
          {:robot {:orientation \E}}
          {:robot {:orientation \S}}
          {:robot {:orientation \W}}
          {:robot {:orientation \N}}]
         (reductions process-instruction
                     {:robot {:orientation \N}}
                     (seq "RRRR")))))

(deftest go-in-a-right-circle
  (is (= [{:world {:width 3, :height 3}, :robot {:x 1, :y 1, :orientation \E}}
          {:world {:width 3, :height 3}, :robot {:x 1, :y 1, :orientation \S}}
          {:world {:width 3, :height 3}, :robot {:x 1, :y 0, :orientation \S}}
          {:world {:width 3, :height 3}, :robot {:x 1, :y 0, :orientation \W}}
          {:world {:width 3, :height 3}, :robot {:x 0, :y 0, :orientation \W}}
          {:world {:width 3, :height 3}, :robot {:x 0, :y 0, :orientation \N}}
          {:world {:width 3, :height 3}, :robot {:x 0, :y 1, :orientation \N}}
          {:world {:width 3, :height 3}, :robot {:x 0, :y 1, :orientation \E}}
          {:world {:width 3, :height 3}, :robot {:x 1, :y 1, :orientation \E}}]
         (reductions process-instruction
                     {:world {:width 3 :height 3}
                      :robot {:x 1 :y 1 :orientation \E}}
                     (seq "RFRFRFRF")))))

(comment
  (run-tests)
  )
