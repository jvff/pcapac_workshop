OpenCL C
========

- Based on C99
- Some restrictions
    - No function pointers
    - Limited standard library
- Some extensions
    - Vector types (`char8`, `float4`, etc.)
    - Extra functions
- Some differences
    - `int` is always 32 bits
- Extra keywords
    - `__kernel` defines a function that's a compute kernel
    - Memory specific keywords
