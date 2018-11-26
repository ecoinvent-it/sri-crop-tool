package com.quantis_intl.lcigenerator.model;

public class RegistrationRequest
{
    private Integer id;
    private String name;
    private String username;
    private String company;
    private String address;
    private String mail;

    public RegistrationRequest(String name, String username, String company, String address, String mail)
    {
        this.name = name;
        this.username = username;
        this.company = company;
        this.address = address;
        this.mail = mail;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getCompany()
    {
        return company;
    }

    public void setCompany(String company)
    {
        this.company = company;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getMail()
    {
        return mail;
    }

    public void setMail(String mail)
    {
        this.mail = mail;
    }
}
