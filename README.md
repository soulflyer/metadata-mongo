# metadata-mongo

A Clojure library designed to read the metadata from image files and store it in a mongo database. The images provided must be at a path that ends in /yyyy/mm/project/image.xxx The path is used to create the year month and project fields in the database. The id of the record created will be a concatenation of these. These fields will also be stored in the record.

## Usage

save-meta database-name collection-name image.jpg

or

save-meta database-name collection-name directory

## License

Copyright © 2015 Iain Wood

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

## TODO

Add a call to add-missing-keywords from image-lib
