class RegistrationRequest {
  int id;
  String name;
  String username;
  String company;
  String address;
  String mail;

  RegistrationRequest(String name, String username, String company, String address, String mail) {
    this.name = name;
    this.username = username;
    this.company = company;
    this.address = address;
    this.mail = mail;
  }

  Map toJson() => {"name": name, "username": username, "company": company, "address": address, "mail": mail};
}
