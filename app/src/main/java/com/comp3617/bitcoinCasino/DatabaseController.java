package com.comp3617.bitcoinCasino;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;


public class DatabaseController {

    public static DataModel_PriceIndicesNow getTPriceIndicesFromRealmDatabase(Realm realm, long id) {
        RealmResults<DataModel_PriceIndicesNow> results = null;
        DataModel_PriceIndicesNow dataModel_priceIndicesNow = null;

        try {
            RealmQuery<DataModel_PriceIndicesNow> query
                    = realm.where(DataModel_PriceIndicesNow.class);
            query.equalTo("uniqueId", id);

            results = query.findAll();

            if (results != null) {
                dataModel_priceIndicesNow = results.first();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return dataModel_priceIndicesNow;
    }

    public static void setPriceIndicesToRealmDatabase(
            Realm realm,
            long id,
            DataModel_PriceIndicesNow dataModel_priceIndicesNow) {
        try {
            final DataModel_PriceIndicesNow objectToUpdate = new DataModel_PriceIndicesNow(id);

            objectToUpdate.setTime(dataModel_priceIndicesNow.getTime());
            objectToUpdate.setBtc(dataModel_priceIndicesNow.getBtc());
            objectToUpdate.setEth(dataModel_priceIndicesNow.getEth());
            objectToUpdate.setLtc(dataModel_priceIndicesNow.getLtc());
            objectToUpdate.setXrp(dataModel_priceIndicesNow.getXrp());
            objectToUpdate.setZec(dataModel_priceIndicesNow.getZec());

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(objectToUpdate);
                }
            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static ListView_Charts getListView_ChartsFromRealmDatabase(Realm realm, long id) {
        RealmResults<ListView_Charts> results = null;
        ListView_Charts listView_Charts = null;

        try {
            RealmQuery<ListView_Charts> query
                    = realm.where(ListView_Charts.class);
            query.equalTo("uniqueId", id);

            results = query.findAll();

            if (results != null) {
                listView_Charts = results.first();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return listView_Charts;
    }

    public static ArrayList<ListView_Charts> getAllListView_ChartsFromRealmDatabase(Realm realm) {
        RealmResults<ListView_Charts> results = null;
        ListView_Charts listView_Charts = null;

        try {
            RealmQuery<ListView_Charts> query
                    = realm.where(ListView_Charts.class);
            results = query.findAll();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        ArrayList<ListView_Charts> finalResults = new ArrayList<>();

        for (ListView_Charts charts : results) {
            finalResults.add(charts);
        }

        return finalResults;
    }

    public static void setListView_ChartsToRealmDatabase(Realm realm,
                                                         ListView_Charts listView_Charts) {
        try {
            final ListView_Charts objectToUpdate = listView_Charts;

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(objectToUpdate);
                }
            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void deleteListView_ChartsToRealmDatabase(Realm realm, final long id) {
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<ListView_Charts> result = realm.where(ListView_Charts.class)
                            .equalTo("id", id).findAll();
                    result.deleteAllFromRealm();
                }
            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    public static DataModel_UserProfile getTUserProfileFromRealmDatabase(Realm realm, long id) {
        RealmResults<DataModel_UserProfile> results = null;
        DataModel_UserProfile dataModel_userProfile = null;

        try {
            RealmQuery<DataModel_UserProfile> query
                    = realm.where(DataModel_UserProfile.class);
            query.equalTo("uniqueId", id);

            results = query.findAll();

            if (results != null) {
                dataModel_userProfile = results.first();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (dataModel_userProfile == null) {
                return null;
            }
        }

        return dataModel_userProfile;
    }

    public static void setUserProfileToRealmDatabase(
            Realm realm,
            long id,
            DataModel_UserProfile dataModel_userProfile) {
        try {
            final DataModel_UserProfile objectToUpdate = new DataModel_UserProfile(id);

            objectToUpdate.setCash(dataModel_userProfile.getCash());
            objectToUpdate.setCoinBtc(dataModel_userProfile.getCoinBtc());
            objectToUpdate.setCoinLtc(dataModel_userProfile.getCoinLtc());
            objectToUpdate.setCoinEth(dataModel_userProfile.getCoinEth());
            objectToUpdate.setCoinZec(dataModel_userProfile.getCoinZec());
            objectToUpdate.setCoinXrp(dataModel_userProfile.getCoinXrp());

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(objectToUpdate);
                }
            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static double buyMaximumCoins(Realm realm, String currencyCode, double currentPrice) {

        DataModel_UserProfile userProfile = DatabaseController.getTUserProfileFromRealmDatabase(
                realm, DataModel_UserProfile.UNIQUE_ID_THE_USER);
        double userCash = userProfile.getCash();

        // Buy the coin from the cash selected.
        double cashSpent = userCash;
        double coinBoughtAmount = cashSpent / currentPrice;
        double cashLeft = userCash - cashSpent;

        // Make a new user profile
        DataModel_UserProfile newUserProfile = new DataModel_UserProfile();
        newUserProfile.setCoinBtc(userProfile.getCoinBtc());
        newUserProfile.setCoinLtc(userProfile.getCoinLtc());
        newUserProfile.setCoinEth(userProfile.getCoinEth());
        newUserProfile.setCoinZec(userProfile.getCoinZec());
        newUserProfile.setCoinXrp(userProfile.getCoinXrp());

        // Update new cash amount.
        newUserProfile.setCash(cashLeft);

        // Update new coin amount.
        if (currencyCode.equalsIgnoreCase("BTC")) {
            newUserProfile.addCoinBtc(coinBoughtAmount);
        } else if (currencyCode.equalsIgnoreCase("LTC")) {
            newUserProfile.addCoinLtc(coinBoughtAmount);
        } else if (currencyCode.equalsIgnoreCase("ETH")) {
            newUserProfile.addCoinEth(coinBoughtAmount);
        } else if (currencyCode.equalsIgnoreCase("ZEC")) {
            newUserProfile.addCoinZec(coinBoughtAmount);
        } else if (currencyCode.equalsIgnoreCase("XRP")) {
            newUserProfile.addCoinXrp(coinBoughtAmount);
        }

        // Save to the database.
        DatabaseController.setUserProfileToRealmDatabase(realm,
                DataModel_UserProfile.UNIQUE_ID_THE_USER, newUserProfile);

        return coinBoughtAmount;
    }

    public static double sellMaximumCoins(Realm realm, String currencyCode, double assetPrice) {

        DataModel_UserProfile userProfile = DatabaseController.getTUserProfileFromRealmDatabase(
                realm, DataModel_UserProfile.UNIQUE_ID_THE_USER);
        double userCash = userProfile.getCash();

        // Update existing coin amount.
        double totalAmount = 0;
        if (currencyCode.equalsIgnoreCase("BTC")) {
            totalAmount = userProfile.getCoinBtc() * assetPrice;
        } else if (currencyCode.equalsIgnoreCase("LTC")) {
            totalAmount = userProfile.getCoinLtc() * assetPrice;
        } else if (currencyCode.equalsIgnoreCase("ETH")) {
            totalAmount = userProfile.getCoinEth() * assetPrice;
        } else if (currencyCode.equalsIgnoreCase("ZEC")) {
            totalAmount = userProfile.getCoinZec() * assetPrice;
        } else if (currencyCode.equalsIgnoreCase("XRP")) {
            totalAmount = userProfile.getCoinXrp() * assetPrice;
        }

        // Buy the coin from the cash selected.
        double cashGained = totalAmount;
        double coinSoldAmount = cashGained / assetPrice;
        double cashLeft = userCash + cashGained;

        // Make a new user profile
        DataModel_UserProfile newUserProfile = new DataModel_UserProfile();
        newUserProfile.setCoinBtc(userProfile.getCoinBtc());
        newUserProfile.setCoinLtc(userProfile.getCoinLtc());
        newUserProfile.setCoinEth(userProfile.getCoinEth());
        newUserProfile.setCoinZec(userProfile.getCoinZec());
        newUserProfile.setCoinXrp(userProfile.getCoinXrp());

        // Update new cash amount.
        newUserProfile.setCash(cashLeft);

        // Update new coin amount.
        if (currencyCode.equalsIgnoreCase("BTC")) {
            newUserProfile.subtractCoinBtc(coinSoldAmount);
        } else if (currencyCode.equalsIgnoreCase("LTC")) {
            newUserProfile.subtractCoinLtc(coinSoldAmount);
        } else if (currencyCode.equalsIgnoreCase("ETH")) {
            newUserProfile.subtractCoinEth(coinSoldAmount);
        } else if (currencyCode.equalsIgnoreCase("ZEC")) {
            newUserProfile.subtractCoinZec(coinSoldAmount);
        } else if (currencyCode.equalsIgnoreCase("XRP")) {
            newUserProfile.subtractCoinXrp(coinSoldAmount);
        }

        // Save to the database.
        DatabaseController.setUserProfileToRealmDatabase(realm,
                DataModel_UserProfile.UNIQUE_ID_THE_USER, newUserProfile);

        return coinSoldAmount;

    }
}
