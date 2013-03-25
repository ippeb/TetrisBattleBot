// Complile using the following command line:
// gcc -Wall -o test test.c -framework ApplicationServices

#include <ApplicationServices/ApplicationServices.h>
#include <time.h>
#include "delay.h"

int main() {
  CGEventRef mkey = CGEventCreateKeyboardEvent(NULL, (CGKeyCode)7, true);
  CGEventRef fkey = CGEventCreateKeyboardEvent(NULL, (CGKeyCode)7, false);
  CGEventPost(kCGHIDEventTap, mkey);
  nanosleep((struct timespec[]){{0, DELAY}}, NULL);
  CGEventPost(kCGHIDEventTap, fkey);
  CFRelease(mkey);
  CFRelease(fkey);
  return 0;
}
