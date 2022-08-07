(ns day13 
  (:require [clojure.string :as str]
            [clojure.set :as set]))

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

;; generalize code for folding
(defn fold-y [index height [x y]]
  (if (< y index)
    [x y]
    [x
     (- (quot height 2) (- y index))]))

(defn vertical-fold [dots index]
  (let [height (apply max (map second dots))]
    (->> (map #(fold-y index height %) dots)
         (into #{}))))

(defn fold-x [index height [x y]]
  (if (< x index)
    [x y]
    [(- (quot height 2) (- x index))
     y]))

(defn horizontal-fold [dots index]
  (let [height (apply max (map first dots))]
    (->> (map #(fold-x index height %) dots)
         (into #{}))))

; only first fold for now
(defn evaluate-folds [[dots [fold & _]]]
  (let [[axis index] fold]
    (case axis
      "x" (horizontal-fold dots index)
      "y" (vertical-fold dots index))))

(def testinput (read-input "testinput.txt"))
(def dots (first testinput))