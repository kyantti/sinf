# Information systems

# 1. Práctica 1

Still to be documented.

# 2. Práctica 2

## 2.1 Income by year

| Year | Income |
| --- | --- |
| 19 | 34483365.68 |
| 20 | 8670.29 |

<aside>
💡 2019 earnings were 34483365.68$.

</aside>

## 2.2 Income by date (month of the year)

| Date | Income |
| --- | --- |
| 01/19 | 1813586.44 |
| 01/20 | 8670.29 |
| 02/19 | 2202022.42 |
| 03/19 | 2807100.38 |
| 04/19 | 3390670.24 |
| 05/19 | 3152606.75 |
| 06/19 | 2577802.26 |
| 07/19 | 2647775.76 |
| 08/19 | 2244467.88 |
| 09/19 | 2097560.13 |
| 10/19 | 3736726.88 |
| 11/19 | 3199603.2 |
| 12/19 | 4613443.34 |

<aside>
💡 The best month for sales was December. That month the earnings were 4613443.34$.

</aside>

## 2.3 Sales by city

| City | Sales |
| --- | --- |
| Atlanta, GA | 16602 |
| Austin, TX | 11153 |
| Boston, MA | 22528 |
| Dallas, TX | 16730 |
| Los Angeles, CA | 33289 |
| New York City, NY | 27932 |
| Portland, ME | 2750 |
| Portland, OR | 11303 |
| San Francisco, CA | 50239 |
| Seattle, WA | 16553 |

<aside>
💡 The city with the highest number of sales in 2019 was San Francisco, CA, with a total of 50239 sales.

</aside>

## 2.4 Sales by time of the day

| Time | Sales |
| --- | --- |
| 00 | 4428 |
| 01 | 2619 |
| 02 | 1398 |
| 03 | 928 |
| 04 | 937 |
| 05 | 1493 |
| 06 | 2810 |
| 07 | 4556 |
| 08 | 7002 |
| 09 | 9816 |
| 10 | 12308 |
| 11 | 14005 |
| 12 | 14202 |
| 13 | 13685 |
| 14 | 12362 |
| 15 | 11391 |
| 16 | 11662 |
| 17 | 12229 |
| 18 | 13802 |
| 19 | 14470 |
| 20 | 13768 |
| 21 | 12244 |
| 22 | 9899 |
| 23 | 7065 |

<aside>
💡 We must show advertising at 7:00 p.m. to maximize the number of sales of a product, since many people are shopping online at that time.

</aside>

## 2.5 Sales by product

| Product | Sales |
| --- | --- |
| 20in Monitor | 4129 |
| 27in 4K Gaming Monitor | 6244 |
| 27in FHD Monitor | 7550 |
| 34in Ultrawide Monitor | 6199 |
| AA Batteries (4-pack) | 27635 |
| AAA Batteries (4-pack) | 31017 |
| Apple Airpods Headphones | 15661 |
| Bose SoundSport Headphones | 13457 |
| Flatscreen TV | 4819 |
| Google Phone | 5532 |
| LG Dryer | 646 |
| LG Washing Machine | 666 |
| Lightning Charging Cable | 23217 |
| Macbook Pro Laptop | 4728 |
| ThinkPad Laptop | 4130 |
| USB-C Charging Cable | 23975 |
| Vareebadd Phone | 2068 |
| Wired Headphones | 20557 |
| iPhone | 6849 |

<aside>
💡 The best selling product in 2019 was "AAA Batteries (4-Pack)”. I think it is because a lot of products still use AAA Batteries, like the Xbox controller.

</aside>

## Script used to run the Hadoop job

```bash
#!/bin/bash

jar_file="sinf.jar"
input_dir="/input/practica2"
output_dir="/output_pablo"
output_base="/output_exercise"

# Run Hadoop job
hadoop jar $jar_file $input_dir $output_dir

# Function to print and get output for each exercise
print_and_get_output() {
  exercise=$1
  output_file="./exercise$exercise.txt"
  output_path="$output_dir$output_base$exercise/par*"
  
  echo "Output exercise $exercise:"
  hadoop fs -cat $output_path

  # Get the key with the highest value
  highest_key=$(hadoop fs -cat $output_path | sort -k2,2nr -t$'\t' | head -n 1 | cut -f1)
  echo "Key with the highest value: $highest_key"

  hadoop fs -get $output_path $output_file
}

# Loop through exercises 1 to 5
for exercise in {1..5}
do
  print_and_get_output $exercise
done

# Clean up
hadoop fs -rm -r $output_dir
rm -rf ./*.txt
```
