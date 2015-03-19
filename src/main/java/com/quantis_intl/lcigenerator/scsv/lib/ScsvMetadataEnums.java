/***************************************************************************
 * Quantis Sàrl CONFIDENTIAL
 * Unpublished Copyright (c) 2009-2014 Quantis SARL, All Rights Reserved.
 * NOTICE: All information contained herein is, and remains the property of Quantis Sàrl. The intellectual and
 * technical concepts contained herein are proprietary to Quantis Sàrl and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Quantis Sàrl. Access to the source code contained herein is hereby forbidden to anyone
 * except current Quantis Sàrl employees, managers or contractors who have executed Confidentiality and Non-disclosure
 * agreements explicitly covering such access.
 * The copyright notice above does not evidence any actual or intended publication or disclosure of this source code,
 * which includes information that is confidential and/or proprietary, and is a trade secret, of Quantis Sàrl.
 * ANY REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC PERFORMANCE, OR PUBLIC DISPLAY OF OR THROUGH USE OF THIS SOURCE
 * CODE WITHOUT THE EXPRESS WRITTEN CONSENT OF Quantis Sàrl IS STRICTLY PROHIBITED, AND IN VIOLATION OF APPLICABLE LAWS
 * AND INTERNATIONAL TREATIES. THE RECEIPT OR POSSESSION OF THIS SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY
 * OR IMPLY ANY RIGHTS TO REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO MANUFACTURE, USE, OR SELL ANYTHING THAT
 * IT MAY DESCRIBE, IN WHOLE OR IN PART.
 */
package com.quantis_intl.lcigenerator.scsv.lib;

import com.google.common.base.Strings;
import com.google.common.collect.EnumHashBiMap;

// FIXME: Put this in a separated projet, and merge with DBMT
public final class ScsvMetadataEnums
{
    private ScsvMetadataEnums()
    {
    }

    public static final EnumHashBiMap<SimaproVersion, String> versions;
    static
    {
        versions = EnumHashBiMap.create(SimaproVersion.class);
        versions.put(SimaproVersion._7_3, "SimaPro 7.3");
        versions.put(SimaproVersion._8_0_2, "SimaPro 8.0.2");
        versions.put(SimaproVersion._8_0_3_14, "SimaPro 8.0.3.14");
        versions.put(SimaproVersion._8_0_4_26, "SimaPro 8.0.4.26");
        versions.put(SimaproVersion.Nestor_mkI, "Nestor Mk I");
    }

    public static enum SimaproVersion
    {
        UNREAD(false),
        UNREAD_AFTER_8_0_4(true),
        _7_3(false),
        _8_0_2(false),
        _8_0_3_14(false),
        _8_0_4_26(true),
        Nestor_mkI(true);

        private final boolean after8_0_4;

        private SimaproVersion(boolean after8_0_4)
        {
            this.after8_0_4 = after8_0_4;
        }

        public boolean isAfter8_0_4()
        {
            return after8_0_4;
        }

        @Override
        public String toString()
        {
            return Strings.nullToEmpty(versions.get(this));
        }

        public static SimaproVersion fromString(String str)
        {
            SimaproVersion res = versions.inverse().get(str);
            return res == null ? UNREAD : res;
        }
    }

    public static enum ExportType
    {
        UNREAD,
        processes,
        methods
    }

    public static enum CsvFormatVersion
    {
        UNREAD,
        _7_0_0;

        @Override
        public String toString()
        {
            if (this == UNREAD)
                return "Unknown";
            else
                return "7.0.0";
        }

        public static CsvFormatVersion fromString(String str)
        {
            return "7.0.0".equals(str) ? _7_0_0 : UNREAD;
        }
    }

    public static enum CsvSeparator
    {
        UNREAD(','),
        Comma(','),
        Semicolon(';'),
        Tab('\t');

        public final char separator;

        private CsvSeparator(char separator)
        {
            this.separator = separator;
        }
    }

    public static enum DecimalSeparator
    {
        UNREAD('.'),
        Comma(','),
        Dot('.');

        public final char separator;

        private DecimalSeparator(char separator)
        {
            this.separator = separator;
        }

        @Override
        public String toString()
        {
            if (this == UNREAD)
                return "Unread";
            else
                return Character.toString(separator);
        }

        public static DecimalSeparator fromString(String str)
        {
            switch (str)
            {
                case ",":
                    return Comma;
                case ".":
                    return Dot;
                default:
                    return UNREAD;
            }
        }
    }

    private static final String CURRENT_STR = "Current";
    private static final String SELECTION_STR = "Selection";
    private static final String ALL_OF_PROJECT_STR = "All of this project";
    private static final String ALL_STR = "All (including libraries)";

    public static enum SelectionType
    {
        UNREAD("Unread"),
        ABSENT("Absent"),
        Current(CURRENT_STR),
        Selection(SELECTION_STR),
        AllOfThisProject(ALL_OF_PROJECT_STR),
        AllIncludingLibraries(ALL_STR);

        private final String name;

        private SelectionType(String name)
        {
            this.name = name;
        }

        @Override
        public String toString()
        {
            return name;
        }

        public static SelectionType fromString(String str)
        {
            switch (str)
            {
                case CURRENT_STR:
                    return Current;
                case SELECTION_STR:
                    return Selection;
                case ALL_OF_PROJECT_STR:
                    return AllOfThisProject;
                case ALL_STR:
                    return AllIncludingLibraries;
                default:
                    return UNREAD;
            }
        }
    }

    public static class Selection
    {
        public static final Selection ABSENT_SELECTION = new Selection(SelectionType.ABSENT, -1);

        public final SelectionType type;
        public final int nbSelected;

        public Selection(SelectionType type, int nbSelected)
        {
            this.type = type;
            this.nbSelected = nbSelected;
        }

        @Override
        public String toString()
        {
            return type.toString() + " (" + String.valueOf(nbSelected) + ")";
        }
    }

    public static enum OpenedAssetType
    {
        library,
        project
    }

    public static class OpenedAsset
    {
        public final OpenedAssetType type;
        public final String name;

        public OpenedAsset(OpenedAssetType type, String name)
        {
            this.type = type;
            this.name = name;
        }

        @Override
        public String toString()
        {
            return "Open " + type.toString() + ": '" + name + "'";
        }
    }

    // FIXME: Add other languages
    public static enum SimaproLanguage
    {
        UNKNOWN,
        EN;
    }
}
