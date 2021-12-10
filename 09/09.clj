(ns aoc.09
  (:require [clojure.string :as str]
            [clojure.set :as set]))
    (:require [clojure.string :as str]))

(defn parse-line [line]
  (mapv #(-> % str Integer/parseInt) line))

(defn read-input [f]
  (->> (slurp f)
     str/split-lines
     (mapv parse-line)))

(defn neighbor-coords [x y]
  [[(inc x) y] [(dec x) y]
   [x (inc y)] [x (dec y)]])

(defn neighbors [x y v]
  (->> (neighbor-coords x y)
     (map #(get-in v %))
     (filter (complement nil?))))

(defn values-and-neighbors [input]
  (for [x (range (count input)) y (range (count (input 0)))]
    { :value (get-in input [x y])
      :neighbors (neighbors x y input)
      :coords [x y]
      ; ncoords contains invalid coords too
      :ncoords (neighbor-coords x y)}))

(defn less-than-neighbors [{x :value nbr :neighbors}]
  (every? #(< x %) nbr))

(defn part-one [input]
  (->> (values-and-neighbors input)
     (filter less-than-neighbors)
     (map #(-> % :value inc))
     (reduce +)))

(defn neighbor-basins [neighbors basins]
  (-> (for [n neighbors b basins :when (contains? b n)]
        b)
      set))

(defn single-basin [basins]
    (if (= (count basins) 1)
        (first basins)))

(defn update-basins [cur basins]
  "Given a current map and basins, returns updated basins"
  (let [coords (:coords cur)]
    (if-let [nbasins (not-empty (neighbor-basins (:ncoords cur) basins))]
        (if-let [basin (single-basin nbasins)]
            (conj
                (disj basins basin)
                (conj basin coords))
            (conj
                (apply disj basins nbasins)
                (conj (apply
                        set/union
                        (conj nbasins (hash-set coords))))))
        (conj basins (hash-set coords)))))
 
(defn can-be-basin [map]
    (if-not (= (:value map) 9)
        map))

(defn build-basins [input]
  (loop [locs (values-and-neighbors input)
         basins #{}]
    (if (empty? locs)
      basins
      (if-let [cur (can-be-basin (first locs))]
        (recur (rest locs) (update-basins cur basins))
        (recur (rest locs) basins)))))

(defn part-two [input]
    (->> (build-basins input)
         (map count)
         (sort >)
         (take 3)
         (reduce *)))

(println (part-one (read-input "input.txt")))
(println (part-two (read-input "input.txt")))