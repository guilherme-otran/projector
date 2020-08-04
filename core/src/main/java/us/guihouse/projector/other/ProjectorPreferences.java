/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.guihouse.projector.other;

import java.util.prefs.Preferences;

/**
 *
 * @author guilherme
 */
public class ProjectorPreferences {

    private static final String NODE_NAME = "/us/guihouse/projector";
    private static Preferences prefs;

    private static final String SQLITE_FILE = "SQLITE_FILE";
    private static final String BACKGROUND_STATIC_FILE_PATH = "BACKGROUND_FILE_PATH";
    private static final String BACKGROUND_FILE_PATH = "BACKGROUND2_FILE_PATH";
    private static final String BACKGROUND_LOGO_FILE_PATH = "BACKGROUND_LOGO_FILE_PATH";
    private static final String BACKGROUND_OVERLAY_FILE_PATH = "BACKGROUND_OVERLAY_FILE_PATH";
    private static final String BACKGROUND_TYPE = "BACKGROUND_TYPE";
    private static final String BACKGROUND_CROP = "BACKGROUND_CROP";
    private static final String DARKEN_BACKGROUND = "DARKEN_BACKGROUND";
    private static final String WINDOW_CONFIG_FILE = "WINDOW_CONFIG_FILE";
    private static final String CHROMA_SCREEN_FONT_SIZE = "CHROMA_SCREEN_FONT_SIZE";
    private static final String CHROMA_SCREEN_PADDING_BOTTOM = "CHROMA_SCREEN_PADDING_BOTTOM";
    private static final String CHROMA_SCREEN_MIN_PADDING_BOTTOM = "CHROMA_SCREEN_MIN_PADDING_BOTTOM";
    private static final String PROJECTION_LABEL_FONT_SIZE = "PROJECTION_LABEL_FONT_SIZE";

    public static Preferences getPrefs() {
        if (prefs == null) {
            prefs = Preferences.userRoot().node(NODE_NAME);
        }

        return prefs;
    }

    public static String getSqlitePath() {
        return getPrefs().get(SQLITE_FILE, "");
    }

    public static String getWindowConfigFile() {
        return getPrefs().get(WINDOW_CONFIG_FILE, null);
    }

    public static void setWindowConfigFile(String fileName) {
        if (fileName == null) {
            getPrefs().remove(WINDOW_CONFIG_FILE);
        } else {
            getPrefs().put(WINDOW_CONFIG_FILE, fileName);
        }
    }

    public static void setSqlitePath(String path) {
        if (path == null) {
            getPrefs().remove(SQLITE_FILE);
        } else {
            getPrefs().put(SQLITE_FILE, path);
        }
    }

    public static String getBackgroundStaticFilePath() {
        return getPrefs().get(BACKGROUND_STATIC_FILE_PATH, null);
    }

    public static void setBackgroundStaticFilePath(String path) {
        if (path == null) {
            getPrefs().remove(BACKGROUND_STATIC_FILE_PATH);
        } else {
            getPrefs().put(BACKGROUND_STATIC_FILE_PATH, path);
        }
    }

    public static String getBackgroundFilePath() {
        return getPrefs().get(BACKGROUND_FILE_PATH, null);
    }

    public static void setBackgroundFilePath(String path) {
        if (path == null) {
            getPrefs().remove(BACKGROUND_FILE_PATH);
        } else {
            getPrefs().put(BACKGROUND_FILE_PATH, path);
        }
    }

    public static String getBackgroundLogoFilePath() {
        return getPrefs().get(BACKGROUND_LOGO_FILE_PATH, null);
    }

    public static void setBackgroundLogoFilePath(String path) {
        if (path == null) {
            getPrefs().remove(BACKGROUND_LOGO_FILE_PATH);
        } else {
            getPrefs().put(BACKGROUND_LOGO_FILE_PATH, path);
        }
    }

    public static String getBackgroundOverlayFilePath() {
        return getPrefs().get(BACKGROUND_OVERLAY_FILE_PATH, null);
    }

    public static void setBackgroundOverlayFilePath(String path) {
        if (path == null) {
            getPrefs().remove(BACKGROUND_OVERLAY_FILE_PATH);
        } else {
            getPrefs().put(BACKGROUND_OVERLAY_FILE_PATH, path);
        }
    }

    public static String getBackgroundType() {
        return getPrefs().get(BACKGROUND_TYPE, null);
    }

    public static void setBackgroundType(String path) {
        if (path == null) {
            getPrefs().remove(BACKGROUND_TYPE);
        } else {
            getPrefs().put(BACKGROUND_TYPE, path);
        }
    }

    public static boolean getDarkenBackground() {
        return getPrefs().getBoolean(DARKEN_BACKGROUND, true);
    }

    public static void setDarkenBackground(boolean darkenBg) {
        getPrefs().putBoolean(DARKEN_BACKGROUND, darkenBg);
    }

    public static boolean getCropBackground() { return getPrefs().getBoolean(BACKGROUND_CROP, true); }

    public static void setCropBackground(boolean crop) { getPrefs().putBoolean(BACKGROUND_CROP, crop); }

    public static int getChromaFontSize() {
        return getPrefs().getInt(CHROMA_SCREEN_FONT_SIZE, 45);
    }

    public static void setChromaFontSize(int fontSize) {
        getPrefs().putInt(CHROMA_SCREEN_FONT_SIZE, fontSize);
    }

    public static int getChromaPaddingBottom() {
        return getPrefs().getInt(CHROMA_SCREEN_PADDING_BOTTOM, 150);
    }

    public static void setChromaPaddingBottom(int paddingBottom) {
        getPrefs().putInt(CHROMA_SCREEN_PADDING_BOTTOM, paddingBottom);
    }

    public static int getChromaMinPaddingBottom() {
        return getPrefs().getInt(CHROMA_SCREEN_MIN_PADDING_BOTTOM, 64);
    }

    public static void setChromaMinPaddingBottom(int minPaddingBottom) {
        getPrefs().putInt(CHROMA_SCREEN_MIN_PADDING_BOTTOM, minPaddingBottom);
    }

    public static int getProjectionLabelFontSize() {
        return getPrefs().getInt(PROJECTION_LABEL_FONT_SIZE, 112);
    }

    public static void setProjectionLabelFontSize(int size) {
        getPrefs().putInt(PROJECTION_LABEL_FONT_SIZE, size);
    }
}
