(ns mars-rover-clj.core-test
  (:require [clojure.test :refer [deftest is run-tests]]
            [mars-rover-clj.core :refer [process-instruction ignore-lost-robots]]))

(deftest robot-turning-left-cicle
  (is (= [{:robot {:orientation \N}}
          {:robot {:orientation \W}}
          {:robot {:orientation \S}}
          {:robot {:orientation \E}}
          {:robot {:orientation \N}}]
         (reductions (ignore-lost-robots process-instruction)
                     {:robot {:orientation \N}}
                     (seq "LLLL")))))

(deftest robot-turning-right-cicle
  (is (= [{:robot {:orientation \N}}
          {:robot {:orientation \E}}
          {:robot {:orientation \S}}
          {:robot {:orientation \W}}
          {:robot {:orientation \N}}]
         (reductions (ignore-lost-robots process-instruction)
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
         (reductions (ignore-lost-robots process-instruction)
                     {:world {:width 3 :height 3}
                      :robot {:x 1 :y 1 :orientation \E}}
                     (seq "RFRFRFRF")))))

(deftest lost-robot
  (is (= [{:world {:width 6, :height 4},
           :sents #{},
           :robot {:x 3, :y 2, :orientation \N}}
          {:world {:width 6, :height 4},
           :sents #{},
           :robot {:x 3, :y 3, :orientation \N}}
          {:world {:width 6, :height 4},
           :sents #{},
           :robot {:x 3, :y 3, :orientation \E}}
          {:world {:width 6, :height 4},
           :sents #{},
           :robot {:x 3, :y 3, :orientation \S}}
          {:world {:width 6, :height 4},
           :sents #{},
           :robot {:x 3, :y 2, :orientation \S}}
          {:world {:width 6, :height 4},
           :sents #{},
           :robot {:x 3, :y 2, :orientation \E}}
          {:world {:width 6, :height 4},
           :sents #{},
           :robot {:x 3, :y 2, :orientation \N}}
          {:world {:width 6, :height 4},
           :sents #{},
           :robot {:x 3, :y 3, :orientation \N}}
          {:world {:width 6, :height 4},
           :sents #{{:x 3, :y 3}},
           :robot {:x 3, :y 3, :orientation \N, :lost true}}
          {:world {:width 6, :height 4},
           :sents #{{:x 3, :y 3}},
           :robot {:x 3, :y 3, :orientation \N, :lost true}}
          {:world {:width 6, :height 4},
           :sents #{{:x 3, :y 3}},
           :robot {:x 3, :y 3, :orientation \N, :lost true}}
          {:world {:width 6, :height 4},
           :sents #{{:x 3, :y 3}},
           :robot {:x 3, :y 3, :orientation \N, :lost true}}
          {:world {:width 6, :height 4},
           :sents #{{:x 3, :y 3}},
           :robot {:x 3, :y 3, :orientation \N, :lost true}}
          {:world {:width 6, :height 4},
           :sents #{{:x 3, :y 3}},
           :robot {:x 3, :y 3, :orientation \N, :lost true}}]
         (reductions (ignore-lost-robots process-instruction)
                     {:world {:width (inc 5) :height (inc 3)}
                      :sents #{}
                      :robot {:x 3 :y 2 :orientation \N}}
                     (seq "FRRFLLFFRRFLL")))))

(comment
  (run-tests)
  )

