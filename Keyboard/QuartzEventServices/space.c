// Complile using the following command line:
// gcc -Wall -o test test.c -framework ApplicationServices

#include <ApplicationServices/ApplicationServices.h>
#include <time.h>
#include "delay.h"

int main() {
    CGEventRef mkey = CGEventCreateKeyboardEvent(NULL, (CGKeyCode)49, true);
    CGEventRef fkey = CGEventCreateKeyboardEvent(NULL, (CGKeyCode)49, false);
    CGEventPost(kCGSessionEventTap, mkey);
    nanosleep((struct timespec[]){{0, DELAY}}, NULL);
    CGEventPost(kCGSessionEventTap, fkey);
    CFRelease(mkey);
    CFRelease(fkey);
    nanosleep((struct timespec[]){{0, DELAY}}, NULL);
    return 0;
}
