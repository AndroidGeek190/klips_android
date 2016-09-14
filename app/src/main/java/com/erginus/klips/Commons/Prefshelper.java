package com.erginus.klips.Commons;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * Created by paramjeet on 29/9/15.
 */
public class Prefshelper {
    public static final String KEY_PREFS_USER_INFO = "user_info";
    private Context context;
    public static SharedPreferences preferences;

    public Prefshelper(Context context) {
        this.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public Context getContext() {
        return context;
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public void storeUserFirstNameToPreference(String name) {
        Editor edit = getPreferences().edit();
        edit.putString("user_first_name", name);
        edit.commit();

    }
    public void storeUserlastNameToPreference(String name) {
        Editor edit = getPreferences().edit();
        edit.putString("user_last_name", name);
        edit.commit();

    }
    public void storeEmailToPreference(String email) {
    Editor edit = getPreferences().edit();
        edit.putString("user_email", email);
        edit.commit();

    }
    public void storeUserIdToPreference(String userid) {
        Editor edit = getPreferences().edit();
        edit.putString("user_id", userid);
        edit.commit();

    }
    public void storeArtistIdToPreference(String id) {
        Editor edit = getPreferences().edit();
        edit.putString("artist_id", id);
        edit.commit();

    }
    public void storeSecHashToPreference(String sec) {
        Editor edit = getPreferences().edit();
        edit.putString("user_security_hash", sec);
        edit.commit();

    }
    public void storePrimaryContactToPreference(String contact) {
        Editor edit = getPreferences().edit();
        edit.putString("user_primary_contact", contact);
        edit.commit();

    }
    public void storeImageToPreference(String image) {
        Editor edit = getPreferences().edit();
        edit.putString("user_profile_image_url", image);
        edit.commit();

    }
    public void storeLoginWithToPreference(String login) {
        Editor edit = getPreferences().edit();
        edit.putString("login_with", login);
        edit.commit();

    }
    public void storeIstTimeHomeToPreference(boolean login) {
        Editor edit = getPreferences().edit();
        edit.putBoolean("ist_time", login);
        edit.commit();

    }
    public boolean getIstTimefromPrefrences() {

        return getPreferences().getBoolean("ist_time", true);
    }
    public void storeWheelItemToPreference(String a) {
        Editor edit = getPreferences().edit();
        edit.putString("wheel", a);
        edit.commit();

    }

    public String getWheelItemfromPrefrences() {

        return getPreferences().getString("wheel", "");
    }
    public void storeCountryIdToPreference(String countryId) {
        Editor edit = getPreferences().edit();
        edit.putString("countries_id", countryId);
        edit.commit();

    }
    public void storeLanguageToPreference(String lang) {
        Editor edit = getPreferences().edit();
        edit.putString("video_language", lang);
        edit.commit();

    }
    public void storeStatusIdToPreference(String id) {
        Editor edit = getPreferences().edit();
        edit.putString("status_id", id);
        edit.commit();

    }
    public String getStatusIdFromPreference() {

        return getPreferences().getString("status_id", "");
    }
    public void storeFavStatusIdToPreference(String id) {
        Editor edit = getPreferences().edit();
        edit.putString("song_favourite_status", id);
        edit.commit();

    }
    public String getFavStatusIdFromPreference() {

        return getPreferences().getString("song_favourite_status", "");
    }
    public void storeRandomVideo(String id) {
        Editor edit = getPreferences().edit();
        edit.putString("random", id);
        edit.commit();

    }
    public String getRandomVideo() {

        return getPreferences().getString("random", "");
    }

    public void storeCurrentPositionOfPlayer(String id) {
        Editor edit = getPreferences().edit();
        edit.putString("current_song", id);
        edit.commit();

    }
    public String getCurrentPositionOfPlayer() {

        return getPreferences().getString("current_song", "");
    }
    public String getLanguageFromPreference() {

        return getPreferences().getString("video_language", "");
    }
    public void storeSLanguageToPreference(String lang) {
        Editor edit = getPreferences().edit();
        edit.putString("song_language", lang);
        edit.commit();

    }
    public void storeUserLanguageToPreference(String lang) {
        Editor edit = getPreferences().edit();
        edit.putString("user_language", lang);
        edit.commit();

    }
    public String getUserLanguageFromPreference() {

        return getPreferences().getString("user_language", "");
    }
    public String getSLanguageFromPreference() {

        return getPreferences().getString("song_language", "");
    }
    public String getCountryIdFromPreference() {

        return getPreferences().getString("countries_id", "");
    }
    public String getLoginWithFromPreference() {

        return getPreferences().getString("login_with", "");
    }
    public String getUserIdFromPreference() {

        return getPreferences().getString("user_id", "");
    }
    public String getArtistIdFromPreference() {

        return getPreferences().getString("artist_id", "");
    }

    public String getUserSecHashFromPreference() {
        return getPreferences().getString("user_security_hash", "");
    }

    public String getUserFNameFromPreference() {
        return getPreferences().getString("user_first_name", "");
    }
    public String getUserLNameFromPreference() {
        return getPreferences().getString("user_last_name", "");
    }
    public String getUserEmailFromPreference() {
        return getPreferences().getString("user_email", "");
    }
    public String getUserContactFromPreference() {
        return getPreferences().getString("user_primary_contact", "");
    }
    public String getImageFromPreference() {
        return getPreferences().getString("user_profile_image_url", "");
    }
}
