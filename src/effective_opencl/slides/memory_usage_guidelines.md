Memory Usage Guidelines
=======================

1. Minimize transfers between host and device
    - PCI-Express 4.0 x16: **< 32 GB/s**
    - GPU memory (AMD Radeon RX 470): **> 200 GB/s**
2. Prefer constant memory over global memory
3. Use local memory for data shared within a work group
4. Always prefer private memory
5. Fine-tune amount of private memory used to avoid global memory spills
