(ns google-dfp-clj.dfp
  (:import com.google.api.ads.common.lib.auth.OfflineCredentials
           com.google.api.ads.dfp.axis.factory.DfpServices
           com.google.api.ads.dfp.lib.client.DfpSession$Builder
           com.google.api.ads.common.lib.auth.OfflineCredentials$Builder
           com.google.api.ads.common.lib.auth.OfflineCredentials$Api
           com.google.api.ads.dfp.axis.v201605.LineItemServiceInterface
           com.google.api.ads.dfp.axis.utils.v201605.StatementBuilder
           com.google.api.ads.dfp.axis.v201605.NetworkServiceInterface))

(defn create-dfp-session []
  (let [credential
        (-> (OfflineCredentials$Builder.)
            (.forApi (OfflineCredentials$Api/DFP))
            (.withJsonKeyFilePath "credentials.json")
            .build
            .generateCredential)]

    (-> (DfpSession$Builder.)
        (.withOAuth2Credential credential)
        (.withApplicationName "myapp")
        (.withNetworkCode "1031683")
        .build)))

(defn print-network []
  (let [session (create-dfp-session)
        services (DfpServices.)
        network-service (.get services session NetworkServiceInterface)
        network (.getCurrentNetwork network-service)]
    (prn (.getNetworkCode network) (.getDisplayName network))))

(defn get-custom-targeting [line-item]
  (->> line-item
       .getTargeting
       .getCustomTargeting
       .getChildren
       seq
       (map #(vector (.getKeyId %) (.getOperator %) (.getValueIds %)))))

(defn all-line-items []
  (let [session (create-dfp-session)
        services (DfpServices.)
        line-item-service (.get services session LineItemServiceInterface)
        statement (-> (StatementBuilder.)
                      (.offset (Integer. 0))
                      (.where "orderId = :orderId AND status = :status")
                      (.withBindVariableValue "orderId" "461712003")
                      (.withBindVariableValue "status" "delivering")
                      (.orderBy "id ASC")
                      (.limit StatementBuilder/SUGGESTED_PAGE_LIMIT)
                      .toStatement)]
    (->> statement
         (.getLineItemsByStatement line-item-service)
         .getResults
         seq
         (map get-custom-targeting)
         prn)))
