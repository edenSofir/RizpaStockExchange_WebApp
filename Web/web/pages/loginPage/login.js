$(function(){
    $('input[name="Trader&Admin"]').click(function(){
        var $radio = $(this);
        // if this was previously checked
        if ($radio.data('waschecked') === true)
        {
            $radio.prop('checked', false);
            $radio.data('waschecked', false);
        }
        else
            $radio.data('waschecked', true);
        // remove was checked from other radios
        $radio.siblings('input[name="rad"]').data('waschecked', false);
    });
});



