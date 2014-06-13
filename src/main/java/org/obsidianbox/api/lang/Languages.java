/**
 * This file is part of Obsidian, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2013-2014 ObsidianBox <http://obsidianbox.org/>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.obsidianbox.api.lang;

/**
 * All available language variants found from: http://minecraft.gamepedia.com/Language
 */
public enum Languages {
    /**
     * Afrikaans
     */
    AFRIKAANS("af_ZA"),
    /**
     * Arabic
     */
    ARABIC("ar_SA"),
    /**
     * Armenian
     */
    ARMENIAN("hy_AM"),
    /**
     * Basque
     */
    BASQUE("eu_ES"),
    /**
     * Bulgarian
     */
    BULGARIAN("bg_BG"),
    /**
     * Catalan
     */
    CATALAN("ca_ES"),
    /**
     * Chinese (Simplified)
     */
    CHINESE_SIMPLIFIED("zh_CH"),
    /**
     * Chinese (Traditional)
     */
    CHINESE_TRADITIONAL("zh_TW"),
    /**
     * Cornwall
     */
    CORNWALL("kw_GB"),
    /**
     * Czech
     */
    CZECH("cs_CZ"),
    /**
     * Danish
     */
    DANISH("da_DK"),
    /**
     * Dutch
     */
    DUTCH("nl_NL"),
    /**
     * American English
     */
    ENGLISH_AMERICAN("en_US"),
    /**
     * Australian English
     */
    ENGLISH_AUSTRALIAN("en_AU"),
    /**
     * British English
     */
    ENGLISH_BRITISH("en_GB"),
    /**
     * Canadian English
     */
    ENGLISH_CANADIAN("en_CA"),
    /**
     * Pirate English
     */
    ENGLISH_PIRATE("en_PT"),
    /**
     * Esperanto
     */
    ESPERANTO("eo_UY"),
    /**
     * Estonian
     */
    ESTONIAN("et_EE"),
    /**
     * Finnish
     */
    FINNISH("fi_FI"),
    /**
     * French
     */
    FRENCH("fr_FR"),
    /**
     * Canadian French
     */
    FRENCH_CANADIAN("fr_CA"),
    /**
     * Galician
     */
    GALICIAN("gl_ES"),
    /**
     * Georgian
     */
    GEORGIAN("ka_GE"),
    /**
     * German
     */
    GERMAN("de_DE"),
    /**
     * Greek
     */
    GREEK("el_GR"),
    /**
     * Hebrew
     */
    HEBREW("hr_HR"),
    /**
     * Hindi
     */
    HINDI("hi_IN"),
    /**
     * Hungarian
     */
    HUNGARIAN("hu_HU"),
    /**
     * Icelandic
     */
    ICELANDIC("is_IS"),
    /**
     * Indonesian
     */
    INDONESIAN("id_ID"),
    /**
     * Irish
     */
    IRISH("ga_IE"),
    /**
     * Italian
     */
    ITALIAN("it_IT"),
    /**
     * Japanese
     */
    JAPANESE("ja_JP"),
    /**
     * Klingon
     */
    KLINGON("tlh_AA"),
    /**
     * Korean
     */
    KOREAN("ko_KR"),
    /**
     * Latin
     */
    LATIN("la_VA"),
    /**
     * Latvian
     */
    LATVIAN("lv_LV"),
    /**
     * Lithuanian
     */
    LITHUANIAN("lt_LT"),
    /**
     * Luxembourgish
     */
    LUXEMBOURGISH("lb_LU"),
    /**
     * Malay
     */
    MALAY("ls_MY"),
    /**
     * Maltese
     */
    MALTESE("mt_MT"),
    /**
     * Norwegian (Norsk)
     */
    NORWEGIAN_NORSK("no_NO"),
    /**
     * Norwegian (Norsk Nynorsk)
     */
    NORWEGIAN_NORSK_NYNORSK("nb_NO"),
    /**
     * Occitan
     */
    OCCITAN("cc_CT"),
    /**
     * Polish
     */
    POLISH("pl_PL"),
    /**
     * Portuguese
     */
    PORTUGUESE("pt_PT"),
    /**
     * Brazilian Portuguese
     */
    PORTUGUESE_BRAZILIAN("pt_BR"),
    /**
     * Quenya (High Elvish)
     */
    QUENYA("qya_AA"),
    /**
     * Romanian
     */
    ROMANIAN("ro_RO"),
    /**
     * Russian
     */
    RUSSIAN("ru_RU"),
    /**
     * Serbian
     */
    SERBIAN("sr_RS"),
    /**
     * Slovak
     */
    SLOVAK("sk_SK"),
    /**
     * Slovenian
     */
    SLOVENIAN("sl_SL"),
    /**
     * Spanish
     */
    SPANISH("es_ES"),
    /**
     * Argentinian Spanish
     */
    SPANISH_ARGENTINIAN("es_AR"),
    /**
     * Mexican Spanish
     */
    SPANISH_MEXICAN("es_MX"),
    /**
     * Uruguayan Spanish
     */
    SPANISH_URUGUAYAN("es_UY"),
    /**
     * Venezuelan Spanish
     */
    SPANISH_VENEZUELAN("es_VE"),
    /**
     * Swedish
     */
    SWEDISH("sv_SE"),
    /**
     * Thai
     */
    THAI("th_TH"),
    /**
     * Turkish
     */
    TURKISH("tr_TR"),
    /**
     * Ukrainian
     */
    UKRAINIAN("uk_UA"),
    /**
     * Vietnamese
     */
    VIETNAMESE("vi_VN"),
    /**
     * Welsh
     */
    WELSH("cy_GB");
    private final String value;

    Languages(String value) {
        this.value = value;
    }

    /**
     * Returns the language code
     *
     * @return The string value
     */
    public String value() {
        return value;
    }
}
