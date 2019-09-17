(ns marse-rover-clj.core
  (:require
    [clojure.string :as string]))

(def sample-input
  "5 3
1 1 E
RFRFRFRF
3 2 N
FRRFLLFFRRFLL
0 3 W
LLFFFLFLFL")

(defn- parse-world-def
  [world-def-str]
  (map #(Integer/parseInt %)
       (-> world-def-str
           string/trim
           (string/split #" "))))

(defn parse-robot-data
  [lines]
  (->> lines
       (remove empty?)
       (partition-all 2)
       (map (fn [[robot-def-str instructions-str]]
              (let [[x y orient] (-> robot-def-str
                                     string/trim
                                     (string/split #" "))]
              {:robot-state {:x (Integer/parseInt x)
                             :y (Integer/parseInt y)
                             :orientation orient}
               :instructions (-> instructions-str string/trim seq)})))))


(defn -main [& args]
  []
  ; use repl to keep evaluating
  (let [[top-right-coord & more] (string/split sample-input #"\n") ; (line-seq *in*)
        [top-x top-y] (parse-world-def top-right-coord)
        robot-data    (parse-robot-data more)]
    ;; input parsing done
    ;; [:world {:x 5, :y 3}
    ;;  :robots ({:robot-state {:x 1, :y 1, :orientation "E"},
    ;;            :instructions (\R \F \R \F \R \F \R \F)} 
    ;;           {:robot-state {:x 3, :y 2, :orientation "N"},
    ;;            :instructions (\F \R \R \F \L \L \F \F \R \R \F \L \L)})]
    [:world {:x top-x :y top-y}
     :robots (take 2 robot-data)]))
