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

(deftest go-in-a-right-circle
  (is (= [{:x 1, :y 1, :orientation \E}
          {:x 1, :y 1, :orientation \S}
          {:x 1, :y 0, :orientation \S}
          {:x 1, :y 0, :orientation \W}
          {:x 0, :y 0, :orientation \W}
          {:x 0, :y 0, :orientation \N}
          {:x 0, :y 1, :orientation \N}
          {:x 0, :y 1, :orientation \E}
          {:x 1, :y 1, :orientation \E}]
         (reductions process-instruction
                     {:x 1 :y 1 :orientation \E}
                     (seq "RFRFRFRF")))))
