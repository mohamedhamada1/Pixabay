package uae.enbd.pixabay.utils


import uae.enbd.pixabay.repository.Status

class StatusAndError {
    // response status 'loading , error, success'
    val status: Status

    val throwable: Throwable?
    // error message that come in throwable
    var message: String? = null


    constructor(status: Status, throwable: Throwable?) {
        this.status = status
        this.throwable = throwable
        if (status == Status.ERROR)
            message =  this.throwable?.message
    }

}
