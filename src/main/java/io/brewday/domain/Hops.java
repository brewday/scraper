package io.brewday.domain;

import java.util.List;

public class Hops {

    private String id;
    private String name;
    private String type;
    private String description;
    private String styleGuide;
    private String alsoKnownAs;
    private String country;
    private String storability;

    private PercentRange alphaAcids;
    private PercentRange betaAcids;
    private PercentRange coHumulone;
    private PercentRange myrceneOils;
    private PercentRange humuleneOils;
    private PercentRange caryophylleneOils;
    private PercentRange farneseneOils;

    private List<Hops> substitutes;

    public Hops() {
    }

    public Hops(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStyleGuide() {
        return styleGuide;
    }

    public void setStyleGuide(String styleGuide) {
        this.styleGuide = styleGuide;
    }

    public String getAlsoKnownAs() {
        return alsoKnownAs;
    }

    public void setAlsoKnownAs(String alsoKnownAs) {
        this.alsoKnownAs = alsoKnownAs;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStorability() {
        return storability;
    }

    public void setStorability(String storability) {
        this.storability = storability;
    }

    public PercentRange getAlphaAcids() {
        return alphaAcids;
    }

    public void setAlphaAcids(PercentRange alphaAcids) {
        this.alphaAcids = alphaAcids;
    }

    public PercentRange getBetaAcids() {
        return betaAcids;
    }

    public void setBetaAcids(PercentRange betaAcids) {
        this.betaAcids = betaAcids;
    }

    public PercentRange getCoHumulone() {
        return coHumulone;
    }

    public void setCoHumulone(PercentRange coHumulone) {
        this.coHumulone = coHumulone;
    }

    public PercentRange getMyrceneOils() {
        return myrceneOils;
    }

    public void setMyrceneOils(PercentRange myrceneOils) {
        this.myrceneOils = myrceneOils;
    }

    public PercentRange getHumuleneOils() {
        return humuleneOils;
    }

    public void setHumuleneOils(PercentRange humuleneOils) {
        this.humuleneOils = humuleneOils;
    }

    public PercentRange getCaryophylleneOils() {
        return caryophylleneOils;
    }

    public void setCaryophylleneOils(PercentRange caryophylleneOils) {
        this.caryophylleneOils = caryophylleneOils;
    }

    public PercentRange getFarneseneOils() {
        return farneseneOils;
    }

    public void setFarneseneOils(PercentRange farneseneOils) {
        this.farneseneOils = farneseneOils;
    }

    public List<Hops> getSubstitutes() {
        return substitutes;
    }

    public void setSubstitutes(List<Hops> substitutes) {
        this.substitutes = substitutes;
    }
}
