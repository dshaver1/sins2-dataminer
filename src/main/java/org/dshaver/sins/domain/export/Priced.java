package org.dshaver.sins.domain.export;

import org.dshaver.sins.domain.ingest.unit.ExoticPrice;
import org.dshaver.sins.domain.ingest.unit.Exotics;
import org.dshaver.sins.domain.ingest.unit.Price;

import java.util.Collection;
import java.util.Optional;

import static java.util.FormatProcessor.FMT;

/**
 * This interface is used by the wiki. Any changes here will result in large changes on the wiki as well to remap the
 * fields.
 */
public interface Priced {
    String getCredits();

    void setCredits(String credits);

    String getMetal();

    void setMetal(String metal);

    String getCrystal();

    void setCrystal(String crystal);

    String getAndvar();

    void setAndvar(String andvar);

    String getTauranite();

    void setTauranite(String tauranite);

    String getIndurium();

    void setIndurium(String indurium);

    String getKalanide();

    void setKalanide(String kalanide);

    String getQuarnium();

    void setQuarnium(String quarnium);

    default void setPrices(Price gameFilePrices, Collection<ExoticPrice> exoticPrices) {
        if (gameFilePrices != null) {
            Optional.ofNullable(gameFilePrices.getCredits())
                    .map(credits -> FMT."%.0f\{credits}")
                    .ifPresent(this::setCredits);
            Optional.ofNullable(gameFilePrices.getMetal())
                    .map(metal -> FMT."%.0f\{metal}")
                    .ifPresent(this::setMetal);
            Optional.ofNullable(gameFilePrices.getCrystal())
                    .map(crystal -> FMT."%.0f\{crystal}")
                    .ifPresent(this::setCrystal);
        }

        if (exoticPrices != null && !exoticPrices.isEmpty()) {
            exoticPrices.stream()
                    .filter(exoticPrice -> Exotics.economic.name().equals(exoticPrice.getExoticType()))
                    .findAny()
                    .ifPresent(exotic -> setAndvar(String.valueOf(exotic.getCount())));

            exoticPrices.stream()
                    .filter(exoticPrice -> Exotics.offense.name().equals(exoticPrice.getExoticType()))
                    .findAny()
                    .ifPresent(exotic -> setTauranite(String.valueOf(exotic.getCount())));

            exoticPrices.stream()
                    .filter(exoticPrice -> Exotics.defense.name().equals(exoticPrice.getExoticType()))
                    .findAny()
                    .ifPresent(exotic -> setIndurium(String.valueOf(exotic.getCount())));

            exoticPrices.stream()
                    .filter(exoticPrice -> Exotics.utility.name().equals(exoticPrice.getExoticType()))
                    .findAny()
                    .ifPresent(exotic -> setKalanide(String.valueOf(exotic.getCount())));

            exoticPrices.stream()
                    .filter(exoticPrice -> Exotics.ultimate.name().equals(exoticPrice.getExoticType()))
                    .findAny()
                    .ifPresent(exotic -> setQuarnium(String.valueOf(exotic.getCount())));
        }
    }

}
