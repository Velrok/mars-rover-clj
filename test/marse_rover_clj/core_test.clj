(ns marse-rover-clj.core-test
  (:require [clojure.test :refer [deftest is]]
            [marse-rover-clj.core :refer [process-instruction]]))

(deftest robot-turning-left-cicle
  (is (= [{:orientation \N} {:orientation \W} {:orientation \S} {:orientation \E} {:orientation \N}]
         (reductions process-instruction
                     {:orientation \N}
                     (seq "LLLL")))))

(deftest robot-turning-right-cicle
  (is (= [{:orientation \N} {:orientation \E} {:orientation \S} {:orientation \W} {:orientation \N}]
         (reductions process-instruction
                     {:orientation \N}
                     (seq "RRRR")))))

