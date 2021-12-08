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

(defn get-input []
    (->> (slurp *in*)
         (str/split-lines)
         (map parse-input)))

(println
    "Solution to part one:"
    (->> (get-input)
         (map second)
         flatten
         (map count)
         (filter #(contains? #{2 4 3 7} %))
         (count)))
