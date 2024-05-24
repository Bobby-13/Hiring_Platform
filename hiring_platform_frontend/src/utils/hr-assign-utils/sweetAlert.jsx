import Swal from "sweetalert2";

export const sweetalert = (text, actionType, nav) => {
  const swalWithBootstrapButtons = Swal.mixin({
    customClass: {
      confirmButton: "btn btn-success",
      cancelButton: "btn btn-danger",
    },
    buttonsStyling: true,
  });
  swalWithBootstrapButtons
    .fire({
      title: "Are you sure?",
      text: `The round will be ${text.toLowerCase()} after this action!`,
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: `Yes, ${actionType}!`,
      cancelButtonText: "No, cancel!",
      reverseButtons: true,
    })
    .then((result) => {
      if (result.isConfirmed) {
        swalWithBootstrapButtons.fire({
          title: `${text}`,
          icon: "success",
        });
        if (actionType === "generate") nav();
      } else if (
        /* Read more about handling dismissals below */
        result.dismiss === Swal.DismissReason.cancel
      ) {
        swalWithBootstrapButtons.fire({
          title: "Cancelled",
          icon: "error",
        });
      }
    });
};

export const missing = () => {
  Swal.fire({
    title: "Date and Duration?",
    text: "You forgot to fill in date and duration!",
    icon: "question"
  });
}