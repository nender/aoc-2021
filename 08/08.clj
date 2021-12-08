(require '[clojure.string :as str])

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

(defn decode-signal [[signals digits]]
    ;; todo implement
    0000)

(defn descramble-signal [key signal]
    "abcdefg")

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

;; Digits and segment counts
;; 1 - 1
;; 7 - 3
;; 2,3,5 - 5
;; 4 - 4
;; 0,6,9 - 6
;; 8 - 7