// Complile using the following command line:
// gcc -Wall -o test test.c -framework ApplicationServices

#include <ApplicationServices/ApplicationServices.h>
#include <time.h>
#include "delay.h"

int main() {
    CGEventRef mkey = CGEventCreateKeyboardEvent(NULL, (CGKeyCode)126, true);
    CGEventPost(kCGSessionEventTap, mkey);
    CFRelease(mkey);
    
    CGEventRef fkey = CGEventCreateKeyboardEvent(NULL, (CGKeyCode)126, false);
    CGEventPost(kCGSessionEventTap, fkey);
    CFRelease(fkey);
    return 0;
}
