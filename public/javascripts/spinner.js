/**
 * Created by TeoLenovo on 4/18/2017.
 */
(function ($) {
    $('.spinDollars .btn:first-of-type').on('click', function() {
        $('.spinDollars input').val( parseInt($('.spinDollars input').val(), 10) + 1);
    });
    $('.spinDollars .btn:last-of-type').on('click', function() {
        $('.spinDollars input').val( parseInt($('.spinDollars input').val(), 10) - 1);
    });
    $('.spinHours .btn:first-of-type').on('click', function() {
        $('.spinHours input').val( parseInt($('.spinHours input').val(), 10) + 1);
    });
    $('.spinHours .btn:last-of-type').on('click', function() {
        $('.spinHours input').val( parseInt($('.spinHours input').val(), 10) - 1);
    });
})(jQuery);