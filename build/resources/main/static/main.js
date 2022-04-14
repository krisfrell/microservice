var times = [];
var selectedSalon;
var selectedService;
var selectedMaster;
var selectedDate;
var currentUser;

$(document).ready(function() {

    $("#table-service").hide();
    $("#table-master").hide();
    $("#calendar").hide();
    $("#submit").hide();
    $("#remove").hide();
    $("#table-upcoming").hide();
    $("#table-past").hide();
    getCurrentUser()

    $.ajax({
        datatype: 'json',
        url: '/salon',
        success: function(data) {
            $.each(data, function(index) {
                $('#table-salon').append("<tr data-id=" + data[index].id + "><td>" + data[index].salon + "</td></tr>")
            });
            $('tr').click(function() {
                openServices($(this).data("id"));
                selectedSalon = $(this).data("id");
                $("#table-salon").hide();
                $("#table-service").show();
            });
        }
    });

    $("#submit").on('click', function() {
        selectedDate = $('#calendar').val();
        var date = getDate(selectedDate);
        var arr = {
            dateTime: date,
            salon: selectedSalon,
            masterId: selectedMaster,
            service: selectedService,
            userId: currentUser
        };

        $.ajax({
            type: "POST",
            url: '/appointment',
            contentType: "application/json",
            data: JSON.stringify(arr),
            success: function() {
                $('#calendar-row').hide();
                $('#submit').hide();
                $('#success').append("Appointment created successfully!");
            }
        });
    });

      $('#appointments').on('click', function() {
          $("#table-salon").hide();
          $("#table-upcoming").show();
          $("#table-past").show();
          $.ajax({
              datatype: 'json',
              url: '/user/' + currentUser + '/appointment',
              success: function(data) {
                  $("#table-upcoming tr").remove();
                  $("#table-past tr").remove();
                  $("#table-upcoming").append("<tr><th> Upcoming appointments: </th></tr>");
                  $("#table-past").append("<tr><th> Past appointments: </th></tr>");
                  $.each(data, function(index) {
                      var parsedData = JSON.parse(data[index])
                      var date = new Date(parsedData.datetime)
                      if (parsedData.datetime > Date.now()) {
                        $('#table-upcoming').append("<tr data-id=" + parsedData.id + "><td>" + parsedData.salonName +
                                              "<br>" + "Service: " + parsedData.serviceName + "<br>" + "Master: " + parsedData.masterName +
                                              "<br>" + "Date: " + date + "<button onclick=\"deleteAppointment(" + parsedData.id + ")\">Remove</button>" + "</td></tr>")
                      } else {
                        $('#table-past').append("<tr data-id=" + parsedData.id + "><td>" + parsedData.salonName +
                                                                      "<br>" + "Service: " + parsedData.serviceName + "<br>" + "Master: " + parsedData.masterName +
                                                                      "<br>" + "Date: " + date + "</td></tr>")
                      }

                  })
              }
          })
      })
  })

function deleteAppointment(id) {
    $.ajax({
        datatype: 'json',
        type: "DELETE",
        url: "/remove/" + id,
        success: function() {
            alert("Appointment removed");
            location.href ="/index";
        }
    })
}

function openServices(id) {
    $.ajax({
        datatype: 'json',
        url: '/salon/' + id + '/service',
        success: function(data) {
            $("#table-service tr").remove();
            $.each(data, function(index) {
                $('#table-service').append("<tr data-id=" + data[index].id + "><td>" + data[index].service + "</td></tr>")
            });
            $('tr').click(function() {
                openMasters(id, $(this).data("id"));
                selectedService = $(this).data("id");
                $("#table-service").hide();
                $("#table-master").show();
            })
        }
    })
}

function openMasters(salonId, serviceId) {
    $.ajax({
        datatype: 'json',
        url: '/salon/' + salonId + '/service/' + serviceId + '/masters',
        success: function(data) {
            $("#table-master tr").remove();
            $.each(data, function(index) {
                $('#table-master').append("<tr data-id=" + data[index].id + "><td>" + data[index].master + "</td></tr>")
            });
            $('tr').click(function() {
                selectedMaster = $(this).data("id");
                getAppointment(selectedMaster);
                $("#table-master").hide();
            })
        }
    })
}

function getAppointment(id) {
    $.ajax({
        datatype: 'json',
        url: '/master/' + id + '/appointment',
        success: function(data) {
            $.each(data, function(index) {
                var offset = (new Date()).getTimezoneOffset() * 60000;
                times.push(new Date(data[index].dateTime - offset).toISOString().split('.')[0])
            })
            openCalendar();
            $("#submit").show();
        }
    })
}

function getCurrentUser() {
    $.ajax({
        datatype: 'json',
        url: '/user',
        success: function(data) {
            currentUser = data.id;
        }
    })
}

function openCalendar() {
    mobiscroll.setOptions({
        locale: mobiscroll.localeRu,
        theme: 'ios',
        themeVariant: 'light'
    });

    let calendar = {
        controls: ['calendar', 'timegrid'],
        display: 'inline',
        invalid: []
    }

    times.forEach(function(time) {
        calendar.invalid.push({
            start: time,
            end: time
        })
    })
    $('#calendar').mobiscroll().datepicker(calendar);
}

function getDate(value) {
    var date = value.replaceAll('.', '/');
    var sb1 = date.split('/')[0];
    var sb2 = date.split('/')[1];
    date = swapSubstrings(date, sb1, sb2);
    return new Date(date).getTime();
}

function swapSubstrings(string, a, b) {
    return string.replace(
        new RegExp(`(${a}|${b})`, "g"),
        match => match === a ? b : a
    )
}