(ns day15
  (:require [clojure.string :as str]
            [clojure.data.priority-map :refer [priority-map]]))

(defn parse-line [line]
   (mapv #(Character/getNumericValue %) line))

(defn read-input [f]
  (->> (slurp f)
       str/split-lines
       (mapv parse-line)))

(defn neighbors [[y x]]
  [[(inc y) x] [(dec y) x]
   [y (inc x)] [y (dec x)]])

(defn all-coords [graph]
  (let [height (count graph)
        width (count (first graph))]
    (for [y (range height) x (range width)]
      [y x])))

(defn check-neighbors [graph dist Q u]
  (loop [neighbors (filter Q (neighbors u))
         dist dist
         Q Q]
    (if (empty? neighbors)
      [dist Q]
      (let [v (first neighbors)
            alt (+ (dist u ##Inf)
                   (get-in graph v))]
        (if (< alt (dist v ##Inf))
          (recur (rest neighbors)
            (assoc dist v alt)
            (assoc Q v alt))
          (recur (rest neighbors) dist Q))))))

(defn dijkstra [graph source target]
  (loop [dist {source 0}
         Q (into (priority-map)
             (map #(vector % (dist % ##Inf))
               (all-coords graph)))]
    (if (empty? Q)
      (dist target)
      (let [[u _] (peek Q)
            [dist Q] (check-neighbors graph dist (pop Q) u)]
        (recur dist Q)))))

(defn shortest-path [graph]
  (let [max (dec (count graph))]
    (dijkstra graph [0 0] [max max])))

(defn add-risk [a b]
  (let [c (+ a b)]
    (if (< c 10)
      c
      ; this will break if c > 18
      (- c 9))))

(defn embiggen-row [row]
  (-> (for [n (range 5)]
        (map #(add-risk % n) row))
      flatten
      vec))

(defn embiggen [graph]
  (->> (for [n (range 5)
             r graph]
    (-> (map #(add-risk % n) r)
              embiggen-row))
    vec))

(let [input (read-input "input.txt")]
  (println "Part one:" (shortest-path input))
  (println "Part two:" (shortest-path (embiggen input))))