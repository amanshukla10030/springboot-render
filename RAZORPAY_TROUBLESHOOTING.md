# Razorpay Payment Integration Troubleshooting Guide

## Common Issues and Solutions

### 1. "Oops something went wrong payment failed" Error

This error can occur due to several reasons:

#### A. Invalid or Expired API Credentials
**Solution:**
1. Login to your Razorpay Dashboard: https://dashboard.razorpay.com/
2. Go to Settings → API Keys
3. Generate new test/live keys if needed
4. Update `src/main/resources/application.yml`:
   ```yaml
   razorpay:
     key-id: rzp_test_YOUR_KEY_ID
     key-secret: YOUR_KEY_SECRET
   ```

#### B. Test Mode Limitations
**Current Configuration:** Using test keys (`rzp_test_RNvRTAgXHS5KiN`)
- Test cards must be used for test mode
- Test card numbers: https://razorpay.com/docs/payments/payments/test-card-upi-details/

**Recommended Test Cards:**
- **Successful Payment:** 4111 1111 1111 1111 (any future date CVV)
- **Failed Payment:** 4111 1111 1111 1234
- **CVV:** Any 3 digits
- **Expiry:** Any future date

#### C. Network/API Connection Issues
**Check:**
- Internet connectivity
- Firewall not blocking Razorpay API (api.razorpay.com)
- Check application logs for detailed error messages

#### D. Order Creation Failure
**Verify in logs:**
```
Creating Razorpay order for amount: XXXX paise
Razorpay order created successfully: order_XXXXXXXXX
```

If order creation fails, check:
- Razorpay account is active
- API credentials are correct
- Amount is valid (minimum ₹1)

### 2. Signature Verification Failed
**Cause:** Payment response signature doesn't match
**Solution:** Ensure `razorpay.key-secret` is correctly configured

### 3. CSRF Token Issues
**Symptoms:** Form submissions fail with 403 Forbidden
**Solution:** The code now includes CSRF token handling in all form submissions

## Testing Steps

### Step 1: Verify Configuration
Check `application.yml` has valid credentials:
```yaml
razorpay:
  key-id: rzp_test_XXXXXXXXXXXXX
  key-secret: XXXXXXXXXXXXXXXX
```

### Step 2: Check Logs
1. Start the application
2. Navigate to checkout page
3. Check console/logs for:
   ```
   Checkout initiated for booking reference: HGXXXXX
   Creating Razorpay order - Amount: XXXX paise
   Razorpay order created successfully - Order ID: order_XXXXX
   ```

### Step 3: Test Payment Flow
1. Click "Pay with Razorpay" button
2. Razorpay checkout should open (if not, check browser console for errors)
3. Use test card: 4111 1111 1111 1111
4. Enter any future date and CVV
5. Complete payment

### Step 4: Verify Success
Check logs for:
```
Payment success callback received for order: order_XXXXX
Signature verified successfully
Payment completed successfully for booking: HGXXXXX
```

## Enhanced Error Handling

The code now includes:
1. ✅ Proper error event listener for Razorpay
2. ✅ CSRF token support
3. ✅ Signature verification for security
4. ✅ Detailed logging at each step
5. ✅ Better error messages to users
6. ✅ Payment failure tracking in database

## Browser Console Debugging

Open browser developer tools (F12) and check:
1. **Console Tab:** Look for JavaScript errors
2. **Network Tab:** Verify Razorpay API calls
3. Check these log messages:
   - "Opening Razorpay checkout..."
   - "Payment successful:" (on success)
   - "Payment failed:" (on failure)
   - "Payment cancelled by user" (on modal close)

## Common Error Messages

| Error Message | Cause | Solution |
|--------------|-------|----------|
| "Payment initialization failed: Missing order ID" | Order creation failed | Check Razorpay credentials and logs |
| "Failed to open payment gateway" | Razorpay SDK not loaded | Check internet connection |
| "Payment verification failed" | Invalid signature | Verify key-secret is correct |
| "Booking not found for this order" | Order ID mismatch | Check database booking record |

## API Key Verification

To verify your API keys are working:
1. Use Razorpay's API testing tool
2. Or use curl:
   ```bash
   curl -u rzp_test_XXXXX:YOUR_SECRET https://api.razorpay.com/v1/orders
   ```

## Need More Help?

1. Check full application logs in the console
2. Verify all changes are saved and application is restarted
3. Check Razorpay Dashboard → Payments for transaction details
4. Contact Razorpay support: support@razorpay.com

## What Was Fixed

1. **Added payment.failed event handler** - Catches payment failures before modal dismissal
2. **CSRF token support** - All form submissions now include CSRF token
3. **Signature verification** - Added security check for payment success
4. **Better logging** - Detailed logs at each step for debugging
5. **Error validation** - Validates credentials and order ID before processing
6. **Improved error page** - Shows detailed error information to users
7. **Database tracking** - Updates payment status to FAILED when payment fails
