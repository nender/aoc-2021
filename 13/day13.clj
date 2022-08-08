(ns day13 
  (:require [clojure.string :as str]))

(defmacro dbg[x] `(let [x# ~x] (println "dbg:" '~x "=" x#) x#))

(defn split-on [pred coll]
  "Like split-with but does not include the delimiter in the second
  collection"
  [(take-while pred coll)
   (rest (drop-while pred coll))])

(defn parse-coord [coord]
  (->> (str/split coord #",")
       (mapv #(Integer/parseInt %))))

(defn parse-fold [fold]
  (let [[axs n]
        (rest (re-matches #".+([xy])=(\d+)" fold))]
    [axs (Integer/parseInt n)]))

(defn parse [[nums folds]]
  [(set (map parse-coord nums))
   (vec (map parse-fold folds))])

(defn read-input [f]
  (->> (slurp f)
       str/split-lines
       (split-on (complement empty?))
       parse))

(defn fold-y [index [x y]]
  (if (< y index)
    [x y]
    [x
     (- index (- y index))]))

(defn fold-x [index [x y]]
  (if (< x index)
    [x y]
    [(- index (- x index))
     y]))

(defn fold-dots [[axis index] dots]
  (let [folder (case axis
                 "x" #(fold-x index %)
                 "y" #(fold-y index %))]
    (into #{} (map folder dots))))

(defn evaluate-folds [[dots folds]]
  (loop [dots dots
         folds folds]
    (if (empty? folds)
      dots
      (recur
        (fold-dots (first folds) dots)
        (rest folds)))))

(defn print-dots [dots]
  (let [height (apply max (map second dots))
        width (apply max (map first dots))]
    (doseq [y (range (inc height))
            x (range (inc width))]
      (if (contains? dots [x y])
        (print "#")
        (print " "))
      (if (= x width)
        (print "\n")))))

;; exposing the solution to part one again is left as an excercise to the
;reader.
(println "Part two:")
(print-dots (evaluate-folds (read-input "input.txt")))