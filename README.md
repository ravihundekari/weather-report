Interesting facts:

Parsing the data from multiple text file is faster but very hard for interpreting.

By using this lambda expression we can sort the data according to any parameter.

a. weatherList.sort(Comparator.comparing(Weather::getYear)); - sort according to year.

b. weatherList.sort(Comparator.comparing(Weather::getKey)); - sort according to key.

c. weatherList.sort(Comparator.comparing(Weather::getRegionCode));- sort according to region code.

d. weatherList.sort(Comparator.comparing(Weather::getValue));- sort according to value.

e. weatherList.sort(Comparator.comparing(Weather::getWeatherParam));- sort according to weather paramater.

We can also reverse the sorting:

First sort the list using ---> weatherList.sort(Comparator.comparing(Weather::getYear)); then apply ---> Collections.sort(weatherList); This would sort list in reverse order on the basis of year.
