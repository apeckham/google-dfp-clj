(ns google-dfp-clj.dfp
  (:import
   [com.google.api.ads.common.lib.auth.OfflineCredentials]
   [com.google.api.ads.common.lib.auth.OfflineCredentials$Builder]
   [com.google.api.ads.common.lib.auth.OfflineCredentials.Api]
   [com.google.api.ads.common.lib.exception.OAuthException]
   [com.google.api.ads.common.lib.exception.ValidationException]
   [com.google.api.ads.dfp.axis.factory.DfpServices]
   [com.google.api.ads.dfp.axis.v201605.Network]
   [com.google.api.ads.dfp.axis.v201605.NetworkServiceInterface]
   [com.google.api.ads.dfp.lib.client.DfpSession]
   [com.google.api.client.auth.oauth2.Credential]))

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
    services (com.google.api.ads.dfp.axis.factory.DfpServices.)
    network-service (.get services session com.google.api.ads.dfp.axis.v201605.NetworkServiceInterface)
    network (.getCurrentNetwork network-service)]
    (prn (.getNetworkCode network) (.getDisplayName network))))

