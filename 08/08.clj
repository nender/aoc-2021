(require '[clojure.string :as str])
(use 'clojure.set)

(defn split-on [pred coll]
    "Like split-with but does not include the delimiter in the second
    collection"
    [(take-while pred coll)
        (->> (drop-while pred coll) rest)])

(defn parse-input [line]
    (->> (str/split line #" ")
         (split-on #(not= "|" %))
         (map #(map set %))))

(def input
    (->> (slurp *in*)
         (str/split-lines)
         (map parse-input)))

(defn first-length [n coll]
    (->> coll
         (filter #(= (count %) n))
         first))

(defn all-length [n coll]
    (filter #(= (count %) n) coll))

(defn derive-key [signals]
    (let [one (first-length 2 signals)
          four (first-length 4 signals)
          seven (first-length 3 signals)
          eight (first-length 7 signals)
          horizontals (apply intersection (all-length 5 signals))
          a (difference seven one)
          d (intersection
                (difference horizontals seven)
                four)
          b (difference four one d)
          g (difference horizontals (union d a))
          e (difference eight four (union g a))
          two (first
                (filter #(subset? e %)
                (all-length 5 signals)))
          c (difference two horizontals e)
          f (difference one c)]

        (zipmap
            (map first [a b c d e f g])
            [\a \b \c \d \e \f \g]
        )))

(defn descramble-digit [key digit]
    (->> (map #(get key %) digit)
         set))

(defn decode-digit [signal]
    "Given a descrambled signal, return the corresponding digit"
    (-> {#{\a \b \c \e \f \g} \0
         #{\c \f} \1
         #{\a \c \d \e \g} \2
         #{\a \c \d \f \g} \3
         #{\b \c \d \f } \4
         #{\a \b \d \f \g} \5
         #{\a \b \d \e \f \g} \6
         #{\a \c \f} \7
         #{\a \b \c \d \e \f \g} \8
         #{\a \b \c \d \f \g} \9}
         (get signal)))

(defn decode-signal [[signals digits]]
 (let [key (derive-key signals)]
    (->> (map #(descramble-digit key %) digits)
         (map decode-digit)
         str/join
         Integer/parseInt)))

(println
    "Solution to part one:"
    (->> input
         (map second)
         flatten
         (map count)
         (filter #(contains? #{2 4 3 7} %))
         count))

(println
    "Solution to part two:"
    (->> input
         (map decode-signal)
         (reduce +)))

;; (println
;;     (let [signals (first (first input))]
;;         (derive-key signals)))

;; Digits and segment counts
;; 1 - 2
;; 7 - 3
;; 2,3,5 - 5
;; 4 - 4
;; 0,6,9 - 6
;; 8 - 7