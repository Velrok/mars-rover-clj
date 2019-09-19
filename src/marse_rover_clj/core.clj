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

(defn- parse-robot-data
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
                             :orientation (first orient)} ;; string to char
               :instructions (-> instructions-str string/trim seq)})))))

(defmulti process-instruction (fn [_ instr] instr))

(defmethod process-instruction \L
  [state _instr]
  (update-in state
             [:robot :orientation]
             ;; map implements the function interface by performing a key lookup
             {\N \W
              \W \S
              \S \E
              \E \N}))

(defmethod process-instruction \R
  [state _instr]
  (update-in state
             [:robot :orientation]
             {\N \E
              \E \S
              \S \W
              \W \N}))

(defn- on-grid?
  [{:keys [width height]}
   {:keys [x y]}]
  (and (<= 0 x (dec width))
       (<= 0 y (dec height))))

(defmethod process-instruction \F
  [{:keys [world robot] :as state} _instr]
  (let [{:keys [x y orientation]} robot
        [dx dy] ({\N [ 0  1]
                  \W [-1  0]
                  \S [ 0 -1]
                  \E [ 1  0]}
                 orientation)
        x' (+ x dx)
        y' (+ y dy)
        lost? (on-grid? world {:x x' :y y'})]

    (if lost?
      (assoc-in state [:robot :lost] true)
      (-> state
          (assoc-in [:robot :x] x')
          (assoc-in [:robot :y] y')))))


(defn -main [& args]
  []
  ; use repl to keep evaluating
  (let [[top-right-coord & more] (string/split sample-input #"\n") ; (line-seq *in*)
        [max-x max-y] (parse-world-def top-right-coord)
        robot-data    (parse-robot-data more)]
    ;; input parsing done
    ;; [:world {:x 5, :y 3}
    ;;  :robots ({:robot-state {:x 1, :y 1, :orientation "E"},
    ;;            :instructions (\R \F \R \F \R \F \R \F)} 
    ;;           {:robot-state {:x 3, :y 2, :orientation "N"},
    ;;            :instructions (\F \R \R \F \L \L \F \F \R \R \F \L \L)})]

    ;; todo
    ;; [x] setup a robot in the world
    ;; [x] keep reducing over instructions for a robot
    ;; [x] print final state
    ;; [ ] detect robots fallig of the map
    ;; [ ] keep track of lost robots between robot runs (positon and orientaion sent)
    ;; [ ] ignore instructions that got a robot lost before
    (doseq [{:keys [robot-state instructions]} (take 1 robot-data)]
      (let [{:keys [x y orientation lost?]} (reduce process-instruction
                                                    {:world {:width  (inc max-x)
                                                             :height (inc max-y)}
                                                     :robot robot-state}
                                                    instructions)]
        (println x y orientation (if lost? "LOST" ""))))))
