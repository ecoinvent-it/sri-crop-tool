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
package com.quantis_intl.lcigenerator.licence;

import java.time.LocalDate;
import java.time.Period;
import java.util.OptionalInt;

import com.quantis_intl.stack.utils.Qid;

// TODO: Put in a dedicated component
// TODO: Split components (see new QS2 backoffice)
public class License
{
    private Qid id;

    private Qid userId;

    // TODO: Abstract+Extends?
    private LicenseType licenseType;

    private LocalDate startDate;

    private AlcigRentalItem rentalItem;

    // TODO: Include that in rentalItem
    private String price;

    private String comment;

    // Added because I'm pretty sure we will have to giveaway new generations before we have the time to implement a
    // propre way to deal with this
    private int additionalGenerations;

    // Think about resetting this boolean if you add some additionalGenerations
    private boolean isDepleted;

    @SuppressWarnings("unused")
    private License()
    {}

    public License(Qid userId, LicenseType licenseType, LocalDate startDate, AlcigRentalItem rentalItem, String price,
            String comment)
    {
        this.id = Qid.random();
        this.userId = userId;
        this.licenseType = licenseType;
        this.startDate = startDate;
        this.rentalItem = rentalItem;
        this.price = price;
        this.comment = comment;
    }

    public Qid getId()
    {
        return id;
    }

    public void setId(Qid id)
    {
        this.id = id;
    }

    public Qid getUserId()
    {
        return userId;
    }

    public void setUserId(Qid userId)
    {
        this.userId = userId;
    }

    public LicenseType getLicenseType()
    {
        return licenseType;
    }

    public void setLicenseType(LicenseType licenseType)
    {
        this.licenseType = licenseType;
    }

    public LocalDate getStartDate()
    {
        return startDate;
    }

    public void setStartDate(LocalDate startDate)
    {
        this.startDate = startDate;
    }

    public AlcigRentalItem getRentalItem()
    {
        return rentalItem;
    }

    public void setRentalItem(AlcigRentalItem rentalItem)
    {
        this.rentalItem = rentalItem;
    }

    public String getPrice()
    {
        return price;
    }

    public void setPrice(String price)
    {
        this.price = price;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public int getAdditionalGenerations()
    {
        return additionalGenerations;
    }

    public void setAdditionalGenerations(int additionalGenerations)
    {
        this.additionalGenerations = additionalGenerations;
    }

    public boolean getIsDepleted()
    {
        return isDepleted;
    }

    public void setIsDepleted(boolean isDepleted)
    {
        this.isDepleted = isDepleted;
    }

    public LocalDate getEndDate()
    {
        return startDate.plus(rentalItem.validityPeriod);
    }

    public OptionalInt getNumberOfGenerations()
    {
        return rentalItem.availableGenerations == null ? OptionalInt.empty()
                : OptionalInt.of(rentalItem.availableGenerations + additionalGenerations);
    }

    public static enum LicenseType
    {
        NORMAL,
        DEMO,
        FORMATION
    }

    public static enum AlcigRentalItem
    {
        DEMO_3_USES(Period.ofMonths(1), 3),
        UNI_10_USES(Period.ofYears(3), 10),
        UNI_25_USES(Period.ofYears(3), 25),
        UNI_50_USES(Period.ofYears(3), 50),
        UNI_UNLIMITED(Period.ofYears(1), null),
        BUSINESS_10_USES(Period.ofYears(3), 10),
        BUSINESS_25_USES(Period.ofYears(3), 25),
        BUSINESS_50_USES(Period.ofYears(3), 50),
        BUSINESS_UNLIMITED(Period.ofYears(1), null);

        public final Period validityPeriod;

        public final Integer availableGenerations;

        private AlcigRentalItem(Period validityPeriod, Integer availableGenerations)
        {
            this.validityPeriod = validityPeriod;
            this.availableGenerations = availableGenerations;
        }
    }

}
