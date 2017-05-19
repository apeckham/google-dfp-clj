(ns google-dfp-clj.dfp
  (:import com.google.api.ads.common.lib.auth.OfflineCredentials
           com.google.api.ads.dfp.axis.factory.DfpServices
           com.google.api.ads.dfp.axis.v201605.NetworkServiceInterface))

(defn create-dfp-session []

  (let [credential
        (-> (com.google.api.ads.common.lib.auth.OfflineCredentials$Builder.)
            (.forApi (com.google.api.ads.common.lib.auth.OfflineCredentials$Api/DFP))
            (.withJsonKeyFilePath "credentials.json")
            .build
            .generateCredential)]

    (-> (com.google.api.ads.dfp.lib.client.DfpSession$Builder.)
        (.withOAuth2Credential credential)
        (.withApplicationName "myapp")
        (.withNetworkCode "1031683")
        .build)))

(defn run []
  (let
   [session (create-dfp-session)
    services (DfpServices.)
    network-service (.get services session NetworkServiceInterface)
    network (.getCurrentNetwork network-service)]
    (prn (.getNetworkCode network) (.getDisplayName network))))

