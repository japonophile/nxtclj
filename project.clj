(defproject nxtclj "0.1.0-SNAPSHOT"
  :description "Control an NXT 2 robot with Clojure"
  :url "https://github.com/japonophile/nxtclj"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [instaparse "1.3.4"]
                 [lejos.pc/pccomm "0.9.1-beta3"]
                 [net.sf.bluecove/bluecove "2.1.1-SNAPSHOT"]]
  :source-paths      ["src/main/clj"]
  :test-paths        ["src/test/clj"]
  :java-source-paths ["src/main/java"])

