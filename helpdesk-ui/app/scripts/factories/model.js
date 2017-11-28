function License() {
    this.id = undefined;
    this.license = undefined;
    this.passKey = undefined;
    this.activationKey = undefined;
    this.activationLimit = 3;
    this.createdDate = (new Date()).getTime();
    this.xlock = undefined;
    this.numberOfClient = undefined;
    this.schoolName = undefined;
    this.product = new Product();
}

function Product() {
    this.id = undefined;
    this.productName = undefined;
    this.productCode = undefined;
    this.description = undefined;
    this.subModuleType = undefined;
    this.subModuleLable = undefined;
    this.createdDate = undefined;
    this.deleted = undefined;
}

function User() {
    this.name = undefined;
    this.createdDate = (new Date()).getTime();
    this.userName = undefined;
    this.password = undefined;
}

function SubProduct() {
    this.id = undefined;
    this.label = undefined;
    this.value = undefined;
    this.product = new Product();
}