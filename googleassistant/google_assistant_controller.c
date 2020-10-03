#include <stdio.h>
#include <stdlib.h>
int main() {
   // printf() displays the string inside quotation
   printf("Hello, World!");
   int status = system("/home/pi/Documents/Projects/DesktopController/googleassistant/lights_white.sh");
   return 0;
}
