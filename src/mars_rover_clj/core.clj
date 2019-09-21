(ns mars-rover-clj.core
  (:require
    [clojure.java.io :as io]
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
  [{:keys [world robot sents] :as state} _instr]
  (let [{:keys [x y orientation]} robot
        [dx dy] ({\N [ 0  1]
                  \W [-1  0]
                  \S [ 0 -1]
                  \E [ 1  0]}
                 orientation)
        x' (+ x dx)
        y' (+ y dy)
        moving-off-grid (not (on-grid? world {:x x' :y y'}))]

    (cond
      (and moving-off-grid
           (contains?
             (set sents)
             {:x x :y y}))
      ;; we have been warned -> ignore command
      state

      moving-off-grid
      (-> state
          (update-in [:sents]
                     conj
                     (select-keys robot [:x :y]))
          (assoc-in [:robot :lost] true))

      :otherwise
      (-> state
          (assoc-in [:robot :x] x')
          (assoc-in [:robot :y] y')))))

(defn ignore-lost-robots
  "Higher order function, wrapping a fn that can handle instructions,
  but only calls it if the robot is still on the map."
  [process-instruction-fn]
  (fn [state instruction]
    (if (get-in state [:robot :lost])
      state
      (process-instruction-fn
        state
        instruction))))


(defn -main [& args]
  []
  (let [[top-right-coord & more] (line-seq (io/reader *in*))
        [max-x max-y] (parse-world-def top-right-coord)
        robot-data    (parse-robot-data more)
        ;; Todo: validate parsed data conforms to expected schema
        robot-sents   (atom #{})]
    (doseq [{:keys [robot-state instructions]} robot-data]
      (let [{:keys [robot sents]} (reduce (ignore-lost-robots process-instruction)
                                          {:world {:width  (inc max-x)
                                                   :height (inc max-y)}
                                           :sents @robot-sents
                                           :robot robot-state}
                                          instructions)
            {:keys [x y orientation lost]} robot]
        (reset! robot-sents sents) ;; reset global state to include latest findings of this robot
        (println x y orientation (if lost "LOST" ""))))))

;; todo
;; [x] setup a robot in the world
;; [x] keep reducing over instructions for a robot
;; [x] print final state
;; [x] detect robots fallig of the map
;; [x] keep track of lost robots between robot runs (positon and orientaion sent)
;; [x] ignore instructions that got a robot lost before

;; [ ] monitoring
;;     CPU, Memory, 
;;     process time per instruction,
;;     process time per robot,
;;     how many robots are we processing (busines),
;;     how many robots are we loosing (busines)
;; [ ] load test (how many instructions per sec can we process?)

;; [ ] specing robot state and instructions
;; [ ] property testing
;;     - robots always have coordinats that are on the grid given:
;;       - a finat grid
;;       - a sequence of known instructions
;;     - given any of the instructions L or R the robot is in the same positon
;;     - given the instruction F the robot is in a different position or lost
;;     - given a lost robot its state stays the same regardless of the remaining instuction seq
;;     - given a finite grid with sents on all edges a robot will never get lost, given any instruction seq
