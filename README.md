# Pixabay

I just handle to have two different data source and switch from Live to Local in case i get error from Live response.
this approche is a fine just we need to handle how to switch between different data source

this was a required approche to use Local only in case Failure in Live
I used

Room

Lifecycle-aware components

ViewModels

LiveData

Paging

Navigation

ViewBinding

another approche which i will handle it in different branch to using 

a BoundaryCallback

-First, load data into the Room database via the API.

- then want to use that data loaded into Room in the DataSource.Factory that Room will create for you to show data in the list.

Once you get near the end of the available data in Room, you want to load more data into the database and start the process all over again.
